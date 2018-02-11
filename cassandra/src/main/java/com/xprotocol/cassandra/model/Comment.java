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
@Table(value = "comment")
public class Comment {
    
    @PrimaryKeyColumn (name="comment_uuid", type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = DataType.Name.UUID)
    private UUID commentUUID;
    
    @PrimaryKeyColumn (name = "user_uuid", type = PrimaryKeyType.PARTITIONED)
    private UUID userUUID;
    
    @Column(value = "content")
    private String content;
    
    @Column(value = "path")
    private String path;
    
    @Column(value = "protocol_title")
    private String protocolTitle;
    
    @Column(value = "protocol_user_uuid")
    private UUID protocolUserUUID;
    
    @Column(value = "protocol_uuid")
    private UUID protocolUUID;

    public Comment() {
    }

    public UUID getCommentUUID() {
        return commentUUID;
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

    public UUID getProtocolUserUUID() {
        return protocolUserUUID;
    }

    public UUID getProtocolUUID() {
        return protocolUUID;
    }

    public void setCommentUUID(UUID commentUUID) {
        this.commentUUID = commentUUID;
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

    public void setProtocolUserUUID(UUID protocolUserUUID) {
        this.protocolUserUUID = protocolUserUUID;
    }

    public void setProtocolUUID(UUID protocolUUID) {
        this.protocolUUID = protocolUUID;
    }

    public Comment(UUID commentUUID, UUID userUUID, String content, String path, String protocolTitle, UUID protocolUserUUID, UUID protocolUUID) {
        this.commentUUID = commentUUID;
        this.userUUID = userUUID;
        this.content = content;
        this.path = path;
        this.protocolTitle = protocolTitle;
        this.protocolUserUUID = protocolUserUUID;
        this.protocolUUID = protocolUUID;
    }

    @Override
    public String toString() {
        return "Comment{" + "commentUUID=" + commentUUID + ", userUUID=" + userUUID + ", content=" + content + ", path=" + path + ", protocolTitle=" + protocolTitle + ", protocolUserUUID=" + protocolUserUUID + ", protocolUUID=" + protocolUUID + '}';
    }

}