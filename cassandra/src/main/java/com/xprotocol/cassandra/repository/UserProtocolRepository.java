/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.cassandra.repository;

import com.xprotocol.cassandra.model.UserProtocol;
import java.util.List;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tao Zhao
 */
@Repository
public interface UserProtocolRepository extends CrudRepository<UserProtocol, UUID> {
//    @Query("select * from user_protocol where user_uuid = ?0 ")
    List<UserProtocol> findUserProtocolByUserUUID(UUID userUUID);
    
    UserProtocol findUserProtocolByUserUUIDAndUserProtocolUUID(UUID userUUID, UUID userProtocolUUID);
    
    UUID deleteUserProtocolByUserProtocolUUID(UUID userProtocolUUID);
}
