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
@Table(value = "project_by_user")
public class ProjectByUser {
    
    @PrimaryKeyColumn (name="user_project_uuid", type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = DataType.Name.UUID)
    private UUID userProjectUUID;
    
    @Column(value = "user_uuid")
    private UUID userUUID;
    
    @Column(value = "title")
    private String title;

    public ProjectByUser() {
    }

    public UUID getUserProtocolUUID() {
        return userProjectUUID;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getTitle() {
        return title;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ProtocolByUser {" + "userProtocolUUID=" + userProjectUUID + 
                ", \nuserUUID=" + userUUID + ", \ntitle=" + title + "\n}";
    }

    public ProjectByUser(UUID userProjectUUID, UUID userUUID, String title) {
        this.userProjectUUID = userProjectUUID;
        this.userUUID = userUUID;
        this.title = title;
    }
    
}