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
import com.xprotocol.cassandra.repository.UserProjectRepository;
import com.xprotocol.cassandra.repository.UserProtocolRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import com.xprotocol.cassandra.repository.CommentRepository;
import com.xprotocol.cassandra.repository.ProtocolToUserRepository;
import com.xprotocol.service.exceptions.EntityDoesNotExistException;
import com.xprotocol.service.exceptions.EntityUpdateException;
import com.xprotocol.service.exceptions.InvalidCommentKeyException;
import com.xprotocol.utils.Validators;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.data.cassandra.repository.support.BasicMapId;

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
    ProtocolToUserRepository protocolByUserRepo;
    
    @Override
    public Comment updateComment(Comment comment) {
        return commentRepo.save(comment);
    }
    
    @Override
    public List<Comment> findCommentsByUserUUID(String userUUIDStr) {
        return commentRepo.findCommentsByUserUUID(UUID.fromString(userUUIDStr));
    }
    
    @Override
    public void deleteCommentByUserUUIDAndCommentUUID(UUID userUUID, UUID commentUUID){
        commentRepo.delete(BasicMapId.id("userUUID", userUUID).with("commentUUID", commentUUID));
    }
    
    @Override
    public void updateProtocolComments(UserProtocol protocol) throws InvalidCommentKeyException{
        Map<String, String> map = protocol.getComments();
        if(null != map && map.size() > 0){
            for(String key : map.keySet()){
                String[] keyArr = key.split("____");
                if(null == keyArr || keyArr.length != 3){
                    throw new InvalidCommentKeyException("Comment key " + key + " does not have three components!");
                }
                String userUUIDStr = keyArr[0];
                String userName = keyArr[1];
                String commentUUIDStr = keyArr[2];
                if(!Validators.isValidUUIDString(userUUIDStr)){
                    throw new InvalidCommentKeyException("Comment key " + key + " contains invalid userUUID info!");
                }
                if(!Validators.isValidUUIDString(commentUUIDStr)){
                    throw new InvalidCommentKeyException("Comment key " + key + " contains invalid commentUUID info!");
                }
                UUID userUUID = UUID.fromString(userUUIDStr);
                UUID commentUUID = UUID.fromString(commentUUIDStr);
                Comment comment = commentRepo.findCommentByUserUUIDAndCommentUUID(userUUID, commentUUID);
                if(null == comment){
                    try {
                        throw new EntityDoesNotExistException("Cannot find the comment by userUUID="+userUUIDStr+" and commentUUID="+commentUUIDStr);
                    } catch (EntityDoesNotExistException ex) {
                        Logger.getLogger(UserProtocolServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    comment.setContent(map.get(key));
                    comment.setProtocolTitle(protocol.getTitle());
                    comment.setProtocolUUID(protocol.getUserProtocolUUID());
                    comment.setProtocolUserUUID(protocol.getUserUUID());
                    Comment save = commentRepo.save(comment);
                    if(null == save){
                        try {
                            throw new EntityUpdateException("Cannot save the comment with userUUID="+userUUIDStr+" and commentUUID="+commentUUIDStr);
                        } catch (EntityUpdateException ex) {
                            Logger.getLogger(UserProtocolServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
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
    public List<UserProtocol> findProtocolsByUserUUID(String userUUIDStr){ 
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
    public ProtocolToUser updateProtocolToUser(ProtocolToUser protocolToUser){
        return protocolByUserRepo.save(protocolToUser);
    }
    
    @Override
    public ProtocolToUser findProtocolToUser(UUID protocolUUID){
        return protocolByUserRepo.findProtocolToUserByprotocolUUID(protocolUUID);
    }
}
