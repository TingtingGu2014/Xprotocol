/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.service.protocol;

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
    public List<UserProtocol> findProtocolByUserUUID(UUID userUUID);
    public UserProtocol createProtocol(UUID userProtocolUUID, UUID userUUID, String title, String body, Set<UUID> projectUUIDs, 
                                        Set<String> projectTitles, Set<String> files, Map<String, String> comments, Set<String> versions, Set<String> keywords);
}
