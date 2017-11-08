/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.web.mvc;

import com.xprotocol.cassandra.model.UserProtocol;
import com.xprotocol.service.user.UserService;
import com.xprotocol.service.protocol.UserProtocolService;
import com.xprotocol.utils.Validators;
import com.xprotocol.web.exceptions.IncompleteUserProtocolInformationException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tao Zhao
 */

@RestController
@ComponentScan("com.xprotocol.service")
public class UserProtocolController {

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

    @RequestMapping(value="/api/users/{userUUIDStr}/protocols")
    public List<UserProtocol> findAllProtocolsByUser(HttpServletRequest request, @PathVariable("userUUIDStr") String userUUIDStr){
        return protocolSrv.findProtocolByUserUUID(userUUIDStr);
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
                response.sendError(400, "Incomplete or invalid user registration information!");
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
            protocol = protocolSrv.updateProtocol(protocol);
        }
        catch(IncompleteUserProtocolInformationException ex){
            try {
                response.sendError(400, "Incomplete or invalid user registration information!");
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
}
