/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.web.config;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author zhao0677
 */
public class XprotocolWebUtils {
    
    @Autowired
    private static Environment env;
    
    public static String getContextUrlFromRequest(HttpServletRequest request){
        String contextUrl = request.getServletPath();
        if(!contextUrl.startsWith("/") && contextUrl.contains("/")){
            contextUrl = contextUrl.split("/")[1];
        }
        return contextUrl;
    }
    
    public static org.springframework.security.core.userdetails.User getCurrentSessionUser (){
        return (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    public static Collection<GrantedAuthority> getCurrentSessionUserAuthorities(){
        org.springframework.security.core.userdetails.User currentUser = getCurrentSessionUser();
        return currentUser.getAuthorities();
    }
    
    public static boolean currentSessionUserHasAuthority(String role){
        Collection<GrantedAuthority> authorities = getCurrentSessionUserAuthorities();
        Iterator<GrantedAuthority> iterator = authorities.iterator();
        while(iterator.hasNext()) {
            GrantedAuthority auth = iterator.next();
            if(role.equals(auth.getAuthority())){
                return true;
            }
        }
        return false;
    }
    
    public static String getEditorFilePath(String editorUploadDirPath, String fileBaseName) throws IOException{
        String path = null;
        File upldDirFile = new File(editorUploadDirPath);
        if(!upldDirFile.isDirectory()){
            throw new IOException("The editor file upload directory is wrong: "+editorUploadDirPath);
        }
        for (File file : upldDirFile.listFiles()) {
            if(file.getName().contains(fileBaseName)){
                return file.getAbsolutePath();
            }
        }
        return path;
    }
}
