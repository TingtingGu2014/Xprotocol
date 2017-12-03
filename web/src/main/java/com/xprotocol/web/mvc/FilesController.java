/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.web.mvc;

import com.xprotocol.persistence.model.User;
import com.xprotocol.web.config.XprotocolWebUtils;
import com.xprotocol.web.exceptions.UserNotLoggedInException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
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
    
    @PostMapping("/api/editor/images/{protocolUUID}")
    public ResponseEntity<?> uploadEditorImage(@RequestParam("file") MultipartFile uploadfile, @PathVariable("protocolUUID") String userProtocolUUID) {
        
        String location = null;

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.BAD_REQUEST);
        }
        
        if (StringUtils.isEmpty(userProtocolUUID)) {
            return new ResponseEntity("The id of the image is empty!", HttpStatus.BAD_REQUEST);
        }

        try {
            User currentUser = XprotocolWebUtils.getCurrentXprotocolUser();
            String userUUID = currentUser.getUserUUID();
            if(null == currentUser){
                throw new UserNotLoggedInException("You have to log in to be able to upload files!");
            }
            saveUploadedFiles(Arrays.asList(uploadfile), userUUID, userProtocolUUID);
            String fileName = uploadfile.getOriginalFilename();           
            location = "/editor/images/"+userUUID+"/"+userProtocolUUID+"/"+fileName;
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UserNotLoggedInException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity("{\"location\":\"" + location +"\"}", new HttpHeaders(), HttpStatus.OK);

    }

    @RequestMapping(value = "/editor/images/{userUUID}/{userProtocolUUID}/{imageId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadEditorImage(
            @PathVariable("userUUID") String userUUID, 
            @PathVariable("userProtocolUUID") String userProtocolUUID,
            @PathVariable("imageId") String imageId) {
        
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> responseEntity = null;
        InputStream in = null;
        try{
            String filePath = XprotocolWebUtils.getEditorFilePath(editorFileUploadPath, userUUID, userProtocolUUID, imageId);
            File uploadedFile = new File(filePath);
            in = new FileInputStream(uploadedFile);
            byte[] media = IOUtils.toByteArray(in);
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        }
        catch(IOException ex){
            
        }
        finally{
            if(in != null){
                try{
                    in.close();
                }
                catch(IOException ioex){
                    ;
                }
            }
        }
        return responseEntity;
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
    private void saveUploadedFiles(List<MultipartFile> files, String userUUID, String userProtocolUUID) throws IOException {
        
        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }

            byte[] bytes = file.getBytes();            
            String fileName = file.getOriginalFilename();
            String fileFolderPath = editorFileUploadPath + File.separator + userUUID + File.separator + userProtocolUUID + File.separator + "temp";
            File fileFolderPathFile = new File(fileFolderPath);
            if(!fileFolderPathFile.exists()){
                fileFolderPathFile.mkdirs();
            }
            Path path = Paths.get(fileFolderPath + File.separator + fileName);
            Files.write(path, bytes);
        }
    }
}
