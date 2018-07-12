/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.web.mvc;

import com.xprotocol.persistence.model.User;
import com.xprotocol.web.config.XprotocolWebUtils;
import com.xprotocol.web.exceptions.FilePathNotExistsException;
import com.xprotocol.web.exceptions.FileUploadingException;
import com.xprotocol.web.exceptions.InvalidProtocolFileException;
import com.xprotocol.web.exceptions.UserNotLoggedInException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author zhao0677
 * 
 * Example: https://www.mkyong.com/spring-boot/spring-boot-file-upload-example-ajax-and-rest/
 */
@RestController
public class FilesController {
    
    @Value("${editor.fileUploadPath}")
    private String editorFileUploadPath;
    
    @Value("${code.customized.fileDoesNotExist}")
    private int codeFileDoesNotExist;
    
    @PostMapping("/api/users/{userUUID}/protocols/{protocolUUID}/files")
    public ResponseEntity<?> uploadProtocolFiles(            
            @RequestParam("file") MultipartFile uploadfile, 
            @PathVariable("userUUID") String userUUID, 
            @PathVariable("protocolUUID") String userProtocolUUID) 
    {
        
        String location = null;
        String newName = null;
        String originalName = null;

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.BAD_REQUEST);
        }
        
        if (StringUtils.isEmpty(userProtocolUUID)) {
            return new ResponseEntity("The id of the image is empty!", HttpStatus.BAD_REQUEST);
        }

        try {
            User currentUser = XprotocolWebUtils.getCurrentXprotocolUser();
            if(null == currentUser){
                throw new UserNotLoggedInException("You have to log in to be able to upload files!");
            }
            String currentUserUUID = currentUser.getUserUUID();
            if(null == currentUserUUID || !currentUserUUID.equals(userUUID)){
                throw new UserNotLoggedInException("You have to log in to be able to upload files!");
            }
            Map<String, String> nameMap = saveUploadedFiles(Arrays.asList(uploadfile), userUUID, userProtocolUUID);
            for (Map.Entry<String, String> entry : nameMap.entrySet()) {
                originalName = entry.getKey();
                newName = entry.getValue();
                if(newName.startsWith("Error")){
                    throw new FileUploadingException(newName);
                }
                location = "/api/users/"+userUUID+"/protocols/"+userProtocolUUID+"/files/"+newName;
                break;
            }
            
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UserNotLoggedInException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (FileUploadingException ex) {
            Logger.getLogger(FilesController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("newName", newName);
        headers.add("originalName", originalName);
        return new ResponseEntity("{\"location\":\"" + location +"\"}", headers, HttpStatus.OK);

    }

    @RequestMapping(value = "/api/users/{userUUID}/protocols/{userProtocolUUID}/files/{fileName}", method = RequestMethod.GET)
    public void downloadEditorImage(
            HttpServletResponse response,
            @PathVariable("userUUID") String userUUID, 
            @PathVariable("userProtocolUUID") String userProtocolUUID,
            @PathVariable("fileName") String fileName,
            @RequestParam(value = "name", required = false) String originalName ) {
        
        InputStream in = null;
        try{
            String filePath = XprotocolWebUtils.getProtocolFilePath(editorFileUploadPath, userUUID, userProtocolUUID, fileName);
            if(null == filePath){
                throw new FilePathNotExistsException("Cannot find the download file path!");
            }
            XprotocolWebUtils.downloadFileFromServer(response, filePath, originalName);
        }
        catch(IOException ex){
            ex.printStackTrace();
            try {
                response.sendError(500, "IOException:"+ex.getMessage()+"!");
            } catch (IOException ex1) {
                Logger.getLogger(FilesController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (FilePathNotExistsException ex) {
            ex.printStackTrace();
            try {
                response.sendError(500, "File does not exist!");
            } catch (IOException ex1) {
                Logger.getLogger(FilesController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        finally{
            if(in != null){
                try{
                    in.close();
                }
                catch(IOException ioex){
                    ioex.printStackTrace();
                }
            }
        }
    }
    
    @RequestMapping(value="/api/users/{userUUID}/protocols/{userProtocolUUID}/files/{fileName}", method= RequestMethod.DELETE)
    public void deleteProtocolFiles(HttpServletResponse response,
            @PathVariable("userUUID") String userUUID, 
            @PathVariable("userProtocolUUID") String userProtocolUUID,
            @PathVariable("fileName") String fileName) throws IOException{
        try {
            String filePath = XprotocolWebUtils.getProtocolFilePath(editorFileUploadPath, userUUID, userProtocolUUID, fileName);
            if(null == filePath){
                throw new FilePathNotExistsException(("Cannot find file:"+fileName));
            }
            File protocolFile = new File(filePath);
            if(!protocolFile.exists() || protocolFile.isDirectory()){
                throw new InvalidProtocolFileException("Protocol file does not exist or it is a directory!");
            }
            protocolFile.delete();
        } catch (IOException ex) {
            try {
                response.sendError(400, "File IO issue: " + ex.getMessage());
            } catch (IOException ex1) {
                Logger.getLogger(FilesController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (InvalidProtocolFileException ex) {
            response.sendError(400, "Invalid protocol file: " + ex.getMessage());
        } catch (FilePathNotExistsException ex) {
            response.sendError(codeFileDoesNotExist, ex.getMessage());
        }
    }
   
    // 3.1.2 Multiple file upload
//    @PostMapping("/api/editor/images")
//    public ResponseEntity<?> uploadFileMulti(@RequestParam("files") MultipartFile[] uploadfiles) {
//
//        // Get file name
//        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
//                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
//
//        if (StringUtils.isEmpty(uploadedFileName)) {
//            return new ResponseEntity("please select a file!", HttpStatus.BAD_REQUEST);
//        }
//
//        try {
//
//            saveUploadedFiles(Arrays.asList(uploadfiles));
//
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity("Successfully uploaded - " + uploadedFileName, HttpStatus.OK);
//
//    }

//    // 3.1.3 maps html form to a Model
//    @PostMapping("/api/upload/multi/model")
//    public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadModel model) {
//
//        logger.debug("Multiple file upload! With UploadModel");
//
//        try {
//
//            saveUploadedFiles(Arrays.asList(model.getFiles()));
//
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);
//
//    }

    //save file
    private Map<String, String> saveUploadedFiles(List<MultipartFile> files, String userUUID, String userProtocolUUID) throws IOException {
        
        Map<String, String> nameMap = new HashMap<>();
        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }
            try{
                byte[] bytes = file.getBytes();    
                
                String fileName = file.getOriginalFilename().replace(" ", "_wsws_");
                long timeVal = new Timestamp(System.currentTimeMillis()).getTime();
                String extention = FilenameUtils.getExtension(fileName);
                extention = extention.equals("") ? "" : "." + extention;
                String newName = "blobid" + Long.toString(timeVal) + extention; 
                
                String fileFolderPath = editorFileUploadPath + File.separator + userUUID + File.separator + userProtocolUUID + File.separator + "temp";
                File fileFolderPathFile = new File(fileFolderPath);
                if(!fileFolderPathFile.exists()){
                    fileFolderPathFile.mkdirs();
                }
                Path path = Paths.get(fileFolderPath + File.separator + newName);
                Files.write(path, bytes);
                
                nameMap.put(fileName, newName);
            }
            catch(IOException ex){
                nameMap.put(file.getOriginalFilename(), "Error: "+ex.getMessage());
            }
        }
        return nameMap;
    }
}
