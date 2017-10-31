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
@Table(value = "user_project")
public class UserProject {
    
    @PrimaryKeyColumn (name="user_project_uuid", type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = DataType.Name.UUID)
    private UUID userProjectUUID = UUIDs.timeBased();
    
    @PrimaryKeyColumn (name = "user_uuid", type = PrimaryKeyType.PARTITIONED)
    private UUID userUUID;
    
    @Column(value = "title")
    private String title;
    
    @Column(value = "description")
    private String description;
    
    @Column(value = "protocol_uuids")
    private Set<UUID> protocolUUIDs;
    
    @Column(value = "protocol_titles")
    private Set<String> protocolTitles;
    
    @Column(value = "keywords")
    private Set<String> keywords;

    public UserProject() {
    }

    public UUID getUserProjectUUID() {
        return userProjectUUID;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Set<UUID> getProtocolUUIDs() {
        return protocolUUIDs;
    }

    public Set<String> getProtocolTitles() {
        return protocolTitles;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setUserProjectUUID(UUID userProjectUUID) {
        this.userProjectUUID = userProjectUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProtocolUUIDs(Set<UUID> protocolUUIDs) {
        this.protocolUUIDs = protocolUUIDs;
    }

    public void setProtocolTitles(Set<String> protocolTitles) {
        this.protocolTitles = protocolTitles;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public UserProject(UUID userUUID, String title, String description, Set<UUID> protocolUUIDs, Set<String> protocolTitles, Set<String> keywords) {
        this.userUUID = userUUID;
        this.title = title;
        this.description = description;
        this.protocolUUIDs = protocolUUIDs;
        this.protocolTitles = protocolTitles;
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "UserProject{\n" + "userProjectUUID=" + userProjectUUID + ",\n userUUID=" + 
                userUUID + ",\n title=" + title + ",\n description=" + description + ",\n protocolUUIDs=" + 
                protocolUUIDs + ",\n protocolTitles=" + protocolTitles + ",\n keywords=" + keywords + "\n}";
    }
}