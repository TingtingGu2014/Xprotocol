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
 * Find users by protocol UUID
 * 
 * @author zhao0677
 */
@Table(value = "protocol_to_user")
public class ProtocolToUser {
    
    @PrimaryKeyColumn (name="protocol_uuid", type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = DataType.Name.UUID)
    private UUID protocolUUID;
    
    @Column(value = "user_uuid")
    private UUID userUUID;
    
    @Column(value = "title")
    private String title;

    public ProtocolToUser() {
    }

    public UUID getProtocolUUID() {
        return protocolUUID;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getTitle() {
        return title;
    }

    public void setProtocolUUID(UUID protocolUUID) {
        this.protocolUUID = protocolUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ProtocolByUser {" + "protocolUUID=" + protocolUUID + 
                ", \nuserUUID=" + userUUID + ", \ntitle=" + title + "\n}";
    }

    public ProtocolToUser(UUID protocolUUID, UUID userUUID, String title) {
        this.protocolUUID = protocolUUID;
        this.userUUID = userUUID;
        this.title = title;
    }
    
}