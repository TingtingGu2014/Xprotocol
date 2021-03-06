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
import com.xprotocol.service.exceptions.InvalidCommentKeyException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author zhao0677
 */
public interface UserProtocolService {
    
    public Iterable<UserProtocol> findProtocolByUserUUID(UUID userUUID);
    public List<UserProtocol> findProtocolsByUserUUID(String userUUIDStr);
            
    public UserProtocol findProtocolByUserUUIDAndProtocolUUID(UUID userUUID, UUID userProtocolUUID);
    public UserProtocol findProtocolByUserUUIDAndProtocolUUID(String userUUIDStr, String userProtocolUUIDStr);
    public UserProtocol createProtocol(UUID userProtocolUUID, UUID userUUID, String title, String body, Set<UUID> projectUUIDs, 
                                        Set<String> projectTitles, Set<String> files, Map<String, String> comments, Set<String> versions, Set<String> keywords);
    public UserProtocol updateProtocol(UserProtocol protocol);
    
    public UserProject createProject(UUID userProjectUUID, UUID userUUID, String title, String description, Set<UUID> protocolUUIDs, Set<String> protocolTitles, Set<String> keywords);
    
    public Comment updateComment(Comment comment);
    public List<Comment> findCommentsByUserUUID(String userUUIDStr);
    public void deleteCommentByUserUUIDAndCommentUUID(UUID userUUID, UUID commentUUID);
    public void updateProtocolComments(UserProtocol protocol) throws InvalidCommentKeyException;
    
    public ProtocolToUser updateProtocolToUser(ProtocolToUser protocolToUser);
    public ProtocolToUser findProtocolToUser(UUID userProtocolUUID);
}
