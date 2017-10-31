/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.service.protocol;

import com.xprotocol.cassandra.model.UserProtocol;
import com.xprotocol.cassandra.repository.UserProtocolRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhao0677
 */
@Service
@ComponentScan("com.xprotocol.cassandra")
public class UserProtocolServiceImpl implements UserProtocolService {
    
    @Autowired
    UserProtocolRepository protocolRepo;
    
    @Override
    public UserProtocol createProtocol(UUID userProtocolUUID, UUID userUUID, String title, String body, Set<UUID> projectUUIDs, 
                                        Set<String> projectTitles, Set<String> files, Map<String, String> comments, Set<String> versions, Set<String> keywords) {
        return protocolRepo.save(new UserProtocol(userProtocolUUID, userUUID, title, body, projectUUIDs, projectTitles, files, comments, versions, keywords));
    }
    
    @Override
    public List<UserProtocol> findProtocolByUserUUID(UUID userUUID){ 
        return protocolRepo.findUserProtocolByUserUUID(userUUID);
    }
}
