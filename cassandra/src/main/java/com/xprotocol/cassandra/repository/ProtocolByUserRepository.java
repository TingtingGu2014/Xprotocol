/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.cassandra.repository;

import com.xprotocol.cassandra.model.ProtocolByUser;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tao Zhao
 */
@Repository
public interface ProtocolByUserRepository extends CassandraRepository<ProtocolByUser> {
//    @Query("select * from user_details where user_id = ?0 LIMIT ?1 ")
    
}
