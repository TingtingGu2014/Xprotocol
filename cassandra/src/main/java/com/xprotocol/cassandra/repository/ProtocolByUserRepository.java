/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.cassandra.repository;

import com.xprotocol.cassandra.model.ProtocolToUser;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tao Zhao
 */
@Repository
public interface ProtocolByUserRepository extends CassandraRepository<ProtocolToUser> {
//    @Query("select * from user_details where user_id = ?0 LIMIT ?1 ")
    ProtocolToUser findProtocolToUserByprotocolUUID(UUID protocolUUID);
}
