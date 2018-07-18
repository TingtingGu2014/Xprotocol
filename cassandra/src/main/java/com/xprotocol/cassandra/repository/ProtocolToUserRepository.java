/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.cassandra.repository;

import com.xprotocol.cassandra.model.ProtocolToUser;
import java.util.List;
import java.util.UUID;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tao Zhao
 */
@Repository
public interface ProtocolToUserRepository extends CrudRepository<ProtocolToUser, UUID> {
//    @Query("select * from user_details where user_id = ?0 LIMIT ?1 ")
    ProtocolToUser findProtocolToUserByprotocolUUID(UUID protocolUUID);
    
    @Query("select * from xprotocol.protocol_to_user limit :limit")
    List<ProtocolToUser> findProtocolToUserListByLimit(@Param("limit") int limit);
}
