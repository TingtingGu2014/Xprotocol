/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.web.mvc;

import com.xprotocol.cassandra.model.Comment;
import com.xprotocol.cassandra.model.ProtocolToUser;
import com.xprotocol.cassandra.model.UserProtocol;
import com.xprotocol.persistence.model.User;
import com.xprotocol.service.exceptions.InvalidCommentKeyException;
import com.xprotocol.service.user.UserService;
import com.xprotocol.service.protocol.UserProtocolService;
import com.xprotocol.utils.UtilsFileHelper;
import com.xprotocol.utils.Validators;
import com.xprotocol.web.config.XprotocolWebUtils;
import com.xprotocol.web.exceptions.IncompleteCommentInformationException;
import com.xprotocol.web.exceptions.IncompleteUserProtocolInformationException;
import com.xprotocol.web.exceptions.InvalidProtocolFileNameFormatException;
import com.xprotocol.web.exceptions.UserNotLoggedInException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tao Zhao
 */

@RestController
@ComponentScan("com.xprotocol.service")
public class UserProtocolController {
    
    @Value("${editor.fileUploadPath}")
    private String editorFileUploadPath;

    private UserService userSrv;
    
    private UserProtocolService protocolSrv;
    
    @Autowired
    public void setUserProtocolService(UserProtocolService protocolSrv){
        this.protocolSrv = protocolSrv;
    }
    
    @Autowired
    public void setUserService(UserService userSrv){
        this.userSrv = userSrv;
    }

    @RequestMapping(value="/api/users/{userUUIDStr}/protocols", method=RequestMethod.GET)
    public List<UserProtocol> findAllProtocolsByUser(HttpServletRequest request, @PathVariable("userUUIDStr") String userUUIDStr){
        return protocolSrv.findProtocolsByUserUUID(userUUIDStr);
    }
    
    /**
     *
     * @param request : http request
     * @param userUUIDStr : user UUID
     * @param response : http response
     * @return : new user
     */
    @RequestMapping(value="/api/users/{userUUIDStr}/protocols/{userProtocolUUIDStr}", method=RequestMethod.GET)
    public UserProtocol getUserProtocol(HttpServletRequest request, 
                                        @PathVariable("userUUIDStr") String userUUIDStr, 
                                        @PathVariable("userProtocolUUIDStr") String userProtocolUUIDStr,
                                        HttpServletResponse response
                                        )
    {        
        UserProtocol protocol = new UserProtocol();
        try{
            User currentUser = XprotocolWebUtils.getCurrentXprotocolUser();
            if(null == currentUser){
                throw new UserNotLoggedInException("You have to log in to upload files!");
            }
        
            if(Validators.isEmptyString(userUUIDStr)){            
                throw new IncompleteUserProtocolInformationException("The user UUID cannot be empty!");
            }
            else if(Validators.isEmptyString(userProtocolUUIDStr)){
                throw new IncompleteUserProtocolInformationException("The user protocol UUID is empty!");
            }
            protocol = protocolSrv.findProtocolByUserUUIDAndProtocolUUID(userUUIDStr, userProtocolUUIDStr);
        }
        catch(IncompleteUserProtocolInformationException ex){
            try {
                response.sendError(400, "Incomplete or invalid user and or protocol information!");
            } catch (IOException ex1) {
                Logger.getLogger(UserProtocolController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        catch(UserNotLoggedInException ex){
            try {
                response.sendError(400, "Incomplete or invalid user and or protocol information!");
            } catch (IOException ex1) {
                Logger.getLogger(UserProtocolController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        catch(Exception ex){
            try {
                response.sendError(500, "Server error: " + ex.getMessage());
            } catch (IOException ex1) {
                Logger.getLogger(UserProtocolController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return protocol;
    }
    
    /**
     *
     * @param request : http request
     * @param userUUIDStr : user UUID
     * @param userProtocolUUIDStr : protocol UUID
     * @param protocol : protocol to be updated
     * @param response : http response
     * @return : new user
     */
    @RequestMapping(value="/api/users/{userUUIDStr}/protocols/{userProtocolUUIDStr}", method=RequestMethod.POST)
    public UserProtocol updateUserProtocol(HttpServletRequest request, 
                                        @PathVariable("userUUIDStr") String userUUIDStr, 
                                        @PathVariable("userProtocolUUIDStr") String userProtocolUUIDStr,
                                        @RequestBody UserProtocol protocol,
                                        HttpServletResponse response
                                        )
    {
        try{
            if(Validators.isEmptyString(userUUIDStr)){            
                throw new IncompleteUserProtocolInformationException("The user UUID cannot be empty!");
            }
            else if(Validators.isEmptyString(userProtocolUUIDStr)){
                throw new IncompleteUserProtocolInformationException("The user protocol UUID is empty!");
            }
            else if(null == protocol || Validators.isEmptyString(protocol.getTitle())){
                throw new IncompleteUserProtocolInformationException("The protocol data or title is empty!");
            }
            UserProtocol oldProtocol = protocolSrv.findProtocolByUserUUIDAndProtocolUUID(protocol.getUserUUID(), protocol.getUserProtocolUUID());
            UserProtocol newProtocol = protocolSrv.updateProtocol(protocol);
            
            if(null != newProtocol && !oldProtocol.getTitle().equals(newProtocol.getTitle())){
                ProtocolToUser protocolToUser = new ProtocolToUser(newProtocol.getUserProtocolUUID(), newProtocol.getUserUUID(), newProtocol.getTitle());
                protocolSrv.updateProtocolToUser(protocolToUser);
                protocolSrv.updateProtocolComments(newProtocol);
            }
            // Clean up the associted files
            protocolFilesProcess(newProtocol);
        }
        catch(IncompleteUserProtocolInformationException  | InvalidProtocolFileNameFormatException ex){
            try {
                response.sendError(400, "Incomplete or invalid user registration information!"+ex.getMessage());
            } catch (IOException ex1) {
                Logger.getLogger(UserProtocolController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        catch(IOException | InvalidCommentKeyException ex){
            try {
                response.sendError(500, "Server error: " + ex.getMessage());
            } catch (IOException ex1) {
                Logger.getLogger(UserProtocolController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return protocol;
    }
    
    /**
     * Clean up the protocol associated files by moving them to protocol-named folder and delete files left
     * File name example: /editor/images/5ce824f0-bdfe-11e7-9696-0b2512d9785a/blobid1512197832184.jpg____cover.jpg, 
     * Note: every file name ends with a comma!
     * 
     * @param protocol : UserProtocol
     */
    private void protocolFilesProcess(UserProtocol protocol) throws InvalidProtocolFileNameFormatException, IOException{
        String userUUID = protocol.getUserUUID().toString();
        String userProtocolUUID = protocol.getUserProtocolUUID().toString();
        Set<String> files = protocol.getFiles();
        
        if(null != files && files.size() > 0){
            // files with name blobid***
            List<String> codeFileNames = new ArrayList<>();
            // files in the upload folder listed in the protocol
            List<String> containedFiles = new ArrayList<>();
            
            for(String file : files){
                String[] fileArr = file.split("____");
                if(null == fileArr || fileArr.length != 2){
                    throw new InvalidProtocolFileNameFormatException("The file name array does not have two elements!");
                }
                String[] codedFileNameArr = fileArr[0].split("/");
                String codedFileName = codedFileNameArr[codedFileNameArr.length-1];
                codeFileNames.add(codedFileName);
            }
            
            File tempUploadedFileDirFile = new File(editorFileUploadPath + File.separator + userUUID + File.separator + userProtocolUUID + File.separator + "temp");
            if(tempUploadedFileDirFile.exists() && tempUploadedFileDirFile.isDirectory()){
                for(File file : tempUploadedFileDirFile.listFiles()){
                    if(!file.isDirectory() && codeFileNames.contains(file.getName())){
                        containedFiles.add(file.getAbsolutePath());
                    }
                }
            }
            
            // move the protocol listed files to the protocol folder
            if(containedFiles.size() > 0){
                String protocolFilePath = editorFileUploadPath + File.separator + userUUID + File.separator + userProtocolUUID;
                File protocolFileObj = new File(protocolFilePath);
                if(!protocolFileObj.exists() || !protocolFileObj.isDirectory()){
                    protocolFileObj.mkdirs();
                }
                for(String filePath : containedFiles){
                    UtilsFileHelper.moveFile(filePath, protocolFilePath);
                }
            }
            
            File uploadedFileDirFileObj = new File(editorFileUploadPath + File.separator + userUUID + File.separator + userProtocolUUID + File.separator + "temp");
            uploadedFileDirFileObj.delete();
        }
        
        
    }
    
    @RequestMapping(value="/api/users/{userUUIDStr}/comments", method=RequestMethod.GET)
    public List<Comment> findAllCommentsByUser(HttpServletRequest request, @PathVariable("userUUIDStr") String userUUIDStr){
        return protocolSrv.findCommentsByUserUUID(userUUIDStr);
    }
    
    @RequestMapping(value="/api/users/{userUUIDStr}/comments/{commentUUIDStr}", method=RequestMethod.POST)
    public Comment updateComment(HttpServletRequest request, 
                                        @PathVariable("userUUIDStr") String userUUIDStr, 
                                        @PathVariable("commentUUIDStr") String commentUUIDStr,
                                        @RequestBody Comment comment,
                                        HttpServletResponse response
                                        )
    {
        try{
            User currentUser = XprotocolWebUtils.getCurrentXprotocolUser();
            if(null == currentUser){
                throw new UserNotLoggedInException("You have to log in to update comments!");
            }
            if(Validators.isEmptyString(userUUIDStr)){            
                throw new IncompleteCommentInformationException("The user UUID cannot be empty!");
            }
            else if(Validators.isEmptyString(commentUUIDStr)){
                throw new IncompleteCommentInformationException("The comment UUID is empty!");
            }
            else if(null == comment || Validators.isEmptyString(comment.getContent())){
                throw new IncompleteCommentInformationException("The comment body is empty!");
            }
            comment = protocolSrv.updateComment(comment);
            return comment;
        }
        catch(UserNotLoggedInException ex){
            try {
                response.sendError(403, ex.getMessage());
            } catch (IOException ex1) {
                Logger.getLogger(UserProtocolController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        catch(IncompleteCommentInformationException ex){
            try {
                response.sendError(400, "Incomplete or invalid user registration information!"+ex.getMessage());
            } catch (IOException ex1) {
                Logger.getLogger(UserProtocolController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return null;
    }
    
    @RequestMapping(value="/api/users/{userUUIDStr}/comments/{commentUUIDStr}", method=RequestMethod.DELETE)
    public @ResponseBody void deleteComment(HttpServletRequest request, 
                                        @PathVariable("userUUIDStr") String userUUIDStr, 
                                        @PathVariable("commentUUIDStr") String commentUUIDStr,
                                        HttpServletResponse response
                                        ) {
        try{
            User currentUser = XprotocolWebUtils.getCurrentXprotocolUser();
            if(null == currentUser){
                throw new UserNotLoggedInException("You have to log in to delete comments!");
            }
            if(Validators.isEmptyString(userUUIDStr)){            
                throw new IncompleteCommentInformationException("The user UUID cannot be empty!");
            }
            else if(Validators.isEmptyString(commentUUIDStr)){
                throw new IncompleteCommentInformationException("The comment UUID is empty!");
            }
            protocolSrv.deleteCommentByUserUUIDAndCommentUUID(UUID.fromString(userUUIDStr), UUID.fromString(commentUUIDStr));
        }
        catch(UserNotLoggedInException ex){
            try {
                response.sendError(403, ex.getMessage());
            } catch (IOException ex1) {
                Logger.getLogger(UserProtocolController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        catch(IncompleteCommentInformationException ex){
            try {
                response.sendError(400, "Incomplete or invalid user registration information!"+ex.getMessage());
            } catch (IOException ex1) {
                Logger.getLogger(UserProtocolController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        catch(Exception ex){
            try {
                response.sendError(500, ex.getMessage());
            } catch (IOException ex1) {
                Logger.getLogger(UserProtocolController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
