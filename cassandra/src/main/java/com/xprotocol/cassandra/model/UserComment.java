/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.cassandra.model;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.utils.UUIDs;
import java.util.Map;
import java.util.Set;
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
@Table(value = "user_comment")
public class UserComment {
    
    @PrimaryKeyColumn (name="user_comment_uuid", type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = DataType.Name.UUID)
    private UUID userCommentUUID;
    
    @PrimaryKeyColumn (name = "user_uuid", type = PrimaryKeyType.PARTITIONED)
    private UUID userUUID;
    
    @Column(value = "content")
    private String content;
    
    @Column(value = "path")
    private String path;
    
    @Column(value = "protocol_title")
    private String protocolTitle;

    public UserComment() {
    }

    public UUID getUserCommentUUID() {
        return userCommentUUID;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getContent() {
        return content;
    }

    public String getPath() {
        return path;
    }

    public String getProtocolTitle() {
        return protocolTitle;
    }

    public void setUserCommentUUID(UUID userCommentUUID) {
        this.userCommentUUID = userCommentUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setProtocolTitle(String protocolTitle) {
        this.protocolTitle = protocolTitle;
    }

    public UserComment(UUID userCommentUUID, UUID userUUID, String content, String path, String protocolTitle) {
        this.userCommentUUID = userCommentUUID;
        this.userUUID = userUUID;
        this.content = content;
        this.path = path;
        this.protocolTitle = protocolTitle;
    }

    @Override
    public String toString() {
        return "UserComment{\n" + "userCommentUUID=" + userCommentUUID + ",\n userUUID=" + 
                userUUID + ",\n content=" + content + ",\n path=" + path + ",\n protocolTitle=" + protocolTitle + "\n}";
    }

}