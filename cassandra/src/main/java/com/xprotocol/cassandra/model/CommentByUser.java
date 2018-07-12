/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.cassandra.model;

import com.datastax.driver.core.DataType;
import java.util.UUID;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 *
 * @author zhao0677
 */
@Table(value = "comment_by_user")
public class CommentByUser {
    
    @PrimaryKeyColumn (name="user_comment_uuid", type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = DataType.Name.UUID)
    private UUID userCommentUUID;
    
    @Column(value = "user_uuid")
    private UUID userUUID;
    
    @Column(value = "path")
    private String path;

    public CommentByUser() {
    }

    public UUID getUserProtocolUUID() {
        return userCommentUUID;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getPath() {
        return path;
    }

    public void setUserProtocolUUID(UUID userCommentUUID) {
        this.userCommentUUID = userCommentUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public void setCotent(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ProtocolByUser {" + "userProtocolUUID=" + userCommentUUID + 
                ", \nuserUUID=" + userUUID + ", \npath=" + path + "\n}";
    }

    public CommentByUser(UUID userCommentUUID, UUID userUUID, String path) {
        this.userCommentUUID = userCommentUUID;
        this.userUUID = userUUID;
        this.path = path;
    }

}