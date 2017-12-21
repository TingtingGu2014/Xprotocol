/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.service.protocol;

import com.xprotocol.cassandra.model.ProtocolToUser;
import com.xprotocol.cassandra.model.Comment;
import com.xprotocol.cassandra.model.UserProject;
import com.xprotocol.cassandra.model.UserProtocol;
import com.xprotocol.cassandra.repository.ProtocolByUserRepository;
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
import com.xprotocol.cassandra.repository.CommentRepository;

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
    CommentRepository commentRepo;
    
    @Autowired
    UserProjectRepository projectRepo;
    
    @Autowired
    ProtocolByUserRepository protocolByUserRepo;
    
    @Override
    public Comment updateComment(Comment comment) {
        return commentRepo.save(comment);
    }
    
    @Override
    public void deleteCommentByUserUUIDAndCommentUUID(UUID userUUID, UUID commentUUID){
        commentRepo.deleteCommentByUserUUIDAndCommentUUID(userUUID, commentUUID);
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
    
    // *** ProtocolByUser operations ***
    @Override
    public ProtocolToUser updateProtocolByUser(ProtocolToUser protocolByUser){
        return protocolByUserRepo.save(protocolByUser);
    }
    
    @Override
    public ProtocolToUser findProtocolByUser(UUID protocolUUID){
        return protocolByUserRepo.findProtocolToUserByprotocolUUID(protocolUUID);
    }
}
