/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.service.protocol;

import com.xprotocol.cassandra.model.UserComment;
import com.xprotocol.cassandra.model.UserProject;
import com.xprotocol.cassandra.model.UserProtocol;
import com.xprotocol.cassandra.repository.UserCommentRepository;
import com.xprotocol.cassandra.repository.UserProjectRepository;
import com.xprotocol.cassandra.repository.UserProtocolRepository;
import com.xprotocol.service.exceptions.EntityDoesNotExistException;
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
    
    @Autowired
    UserCommentRepository commentRepo;
    
    @Autowired
    UserProjectRepository projectRepo;
    
    @Override
    public UserComment createComment(UUID userCommentUUID, UUID userUUID, String content, String path, String protocolTitle) {
        return commentRepo.save(new UserComment(userCommentUUID, userUUID, content, path, protocolTitle));
    }
    
    @Override
    public UserProject createProject(UUID userProjectUUID, UUID userUUID, String title, String description, Set<UUID> protocolUUIDs, Set<String> protocolTitles, Set<String> keywords) {
        return projectRepo.save(new UserProject(userProjectUUID, userUUID, title, description, protocolUUIDs, protocolTitles, keywords));
    }
    
    @Override
    public UserProtocol createProtocol(UUID userProtocolUUID, UUID userUUID, String title, String body, Set<UUID> projectUUIDs, 
                                        Set<String> projectTitles, Set<String> files, Map<String, String> comments, Set<String> versions, Set<String> keywords) {
        return protocolRepo.save(new UserProtocol(userProtocolUUID, userUUID, title, body, projectUUIDs, projectTitles, files, comments, versions, keywords));
    }
    
    @Override
    public Iterable<UserProtocol> findProtocolByUserUUID(UUID userUUID){ 
        return protocolRepo.findUserProtocolByUserUUID(userUUID);
    }
    
    @Override
    public List<UserProtocol> findProtocolByUserUUID(String userUUIDStr){ 
        return protocolRepo.findUserProtocolByUserUUID(UUID.fromString(userUUIDStr));
    }
    
    @Override
    public UserProtocol findProtocolByUserUUIDAndProtocolUUID(UUID userUUID, UUID userProtocolUUID) {
        return protocolRepo.findUserProtocolByUserUUIDAndUserProtocolUUID(userUUID, userProtocolUUID);
    }
    
    @Override
    public UserProtocol findProtocolByUserUUIDAndProtocolUUID(String userUUIDStr, String userProtocolUUIDStr) {
        return protocolRepo.findUserProtocolByUserUUIDAndUserProtocolUUID(UUID.fromString(userUUIDStr), UUID.fromString(userProtocolUUIDStr));
    }
    
    @Override
    public UserProtocol updateProtocol(UserProtocol protocol) {
        return protocolRepo.save(protocol);
    }
    
}
