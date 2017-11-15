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
@Table(value = "user_protocol")
public class UserProtocol {
    
    @PrimaryKeyColumn (name="user_protocol_uuid", type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = DataType.Name.UUID)
    private UUID userProtocolUUID;
    
    @PrimaryKeyColumn (name = "user_uuid", type = PrimaryKeyType.PARTITIONED)
    private UUID userUUID;
    
    @Column(value = "title")
    private String title;
    
    @Column(value = "body")
    private String body;
    
    @Column(value = "project_uuids")
    private Set<UUID> projectUUIDs;
    
    @Column(value = "project_titles")
    private Set<String> projectTitles;
    
    @Column(value = "files")
    private Set<String> files;
    
    @Column(value = "comments")
    private Map<String, String> comments;
    
    @Column(value = "versions")
    private Set<String> versions;
    
    @Column(value = "keywords")
    private Set<String> keywords;

    public UserProtocol() {
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

    public String getBody() {
        return body;
    }

    public Set<String> getProjectTitles() {
        return projectTitles;
    }

    public Set<UUID> getProjectUUIDs() {
        return projectUUIDs;
    }

    public Set<String> getFiles() {
        return files;
    }

    public Map<String, String> getComments() {
        return comments;
    }

    public Set<String> getVersions() {
        return versions;
    }

    public Set<String> getKeywords() {
        return keywords;
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

    public void setBody(String body) {
        this.body = body;
    }

    public void setProjectUUIDs(Set<UUID> projectUUIDs) {
        this.projectUUIDs = projectUUIDs;
    }
    
    public void setProjectTitles(Set<String> projectTitles) {
        this.projectTitles = projectTitles;
    }

    public void setFiles(Set<String> files) {
        this.files = files;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }

    public void setVersions(Set<String> versions) {
        this.versions = versions;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "UserProtocol{" + "userProtocolUUID=" + userProtocolUUID + 
                ", \nuserUUID=" + userUUID + ", \ntitle=" + title + ", \nbody=" + body 
                + ", \nprojectUUID=" + projectUUIDs + ", \nfiles=" + files + ", \ncomments=" + comments + 
                ", \nversions=" + versions + ", \nkeywords=" + keywords + "\n}";
    }

    
    public UserProtocol(UUID userProtocolUUID, UUID userUUID, String title, String body, Set<UUID> projectUUIDs, Set<String> projectTitles,
                        Set<String> files, Map<String, String> comments, Set<String> versions, Set<String> keywords) {
        this.userProtocolUUID = userProtocolUUID;
        this.userUUID = userUUID;
        this.title = title;
        this.body = body;
        this.projectUUIDs = projectUUIDs;
        this.projectTitles = projectTitles;
        this.files = files;
        this.comments = comments;
        this.versions = versions;
        this.keywords = keywords;
    }
}