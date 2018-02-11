/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.cassandra.repository;

import com.xprotocol.cassandra.model.Comment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.MapId;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zhao0677
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, MapId> {
    List<Comment> findCommentsByUserUUID(UUID userUUID);
    
    Comment findCommentByUserUUIDAndCommentUUID(UUID userUUID, UUID commentUUID);
    
    UUID deleteCommentByUserUUIDAndCommentUUID(UUID userUUID, UUID commentUUID);
    
    @Override
    void delete(Comment comment);
    
    @Override
    void delete(MapId id); 
}
