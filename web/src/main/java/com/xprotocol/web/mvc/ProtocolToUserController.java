/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.web.mvc;

import com.xprotocol.cassandra.model.ProtocolToUser;
import com.xprotocol.persistence.model.User;
import com.xprotocol.service.protocol.ProtocolToUserService;
import com.xprotocol.service.user.UserService;
import com.xprotocol.utils.UtilsStringHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author zhao0677
 */
@RestController
@ComponentScan("com.xprotocol.service")
public class ProtocolToUserController {
    
    private ProtocolToUserService protocolToUserSrv;
    private UserService userSrv;
    
    @Autowired
    public void setProtocolToUserService(ProtocolToUserService protocolToUserSrv){
        this.protocolToUserSrv = protocolToUserSrv;
    }
    
    @Autowired
    public void setUserService(UserService userSrv){
        this.userSrv = userSrv;
    }
    
    @RequestMapping(value="/api/protocolToUsers", method=RequestMethod.GET)
    public Map<String, Object> getRecentProtocols(@RequestParam(value="limit", required=false) String limit, HttpServletResponse response)
    {
        Map<String, Object> protocolToUsersMap = null;
        List<ProtocolToUser> protocolToUsers = null;
        if(UtilsStringHelper.isEmptyString(limit)){
            limit = "20";
        }
        
        try{
            int limitNum = Integer.parseInt(limit);
            protocolToUsers = protocolToUserSrv.findProtocolToUserListByLimit(limitNum);
        }
        catch(NumberFormatException ex){
            try {
                response.sendError(400, "Parameter limit cannot be converted to number!");
            } catch (IOException ex1) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }     
        if(null != protocolToUsers && protocolToUsers.size() > 0){
            protocolToUsersMap = new HashMap<>();
            protocolToUsersMap.put("protocolToUsers", protocolToUsers);
            List<UUID> uuidList = new ArrayList<>();
            for(ProtocolToUser p : protocolToUsers){
                UUID uuid = p.getUserUUID();
                if(null != uuid){
                    System.out.println("uuid = "+uuid.toString());
                    if(!uuidList.contains(uuid)){
                        uuidList.add(uuid);
                    }
                }
            }
            if(uuidList.size() > 0){
                List<User> users = userSrv.findUsersByUUIDs(uuidList);
                if(null != users && users.size() > 0){
                    protocolToUsersMap.put("users", users);
                }
            }
        }
        return protocolToUsersMap;
    }
}
