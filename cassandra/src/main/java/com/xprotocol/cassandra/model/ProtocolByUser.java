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
@Table(value = "protocol_by_user")
public class ProtocolByUser {
    
    @PrimaryKeyColumn (name="user_protocol_uuid", type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = DataType.Name.UUID)
    private UUID userProtocolUUID;
    
    @Column(value = "user_uuid")
    private UUID userUUID;
    
    @Column(value = "title")
    private String title;

    public ProtocolByUser() {
    }

    public UUID getUserProtocolUUID() {
        return userProtocolUUID;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getTitle() {
        return title;
    }

    public void setUserProtocolUUID(UUID userProtocolUUID) {
        this.userProtocolUUID = userProtocolUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ProtocolByUser {" + "userProtocolUUID=" + userProtocolUUID + 
                ", \nuserUUID=" + userUUID + ", \ntitle=" + title + "\n}";
    }

    public ProtocolByUser(UUID userProtocolUUID, UUID userUUID, String title) {
        this.userProtocolUUID = userProtocolUUID;
        this.userUUID = userUUID;
        this.title = title;
    }
    
}