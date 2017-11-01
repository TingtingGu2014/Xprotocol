/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.service.protocol;

import com.xprotocol.cassandra.model.UserComment;
import com.xprotocol.cassandra.model.UserProject;
import com.xprotocol.cassandra.model.UserProtocol;
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
    public UserProtocol findProtocolByUserUUIDAndProtocolUUID(UUID userUUID, UUID userProtocolUUID);
    public UserProtocol createProtocol(UUID userProtocolUUID, UUID userUUID, String title, String body, Set<UUID> projectUUIDs, 
                                        Set<String> projectTitles, Set<String> files, Map<String, String> comments, Set<String> versions, Set<String> keywords);
    
    public UserProject createProject(UUID userProjectUUID, UUID userUUID, String title, String description, Set<UUID> protocolUUIDs, Set<String> protocolTitles, Set<String> keywords);
    
    public UserComment createComment(UUID userCommentUUID, UUID userUUID, String content, String path, String protocolTitle);
}
