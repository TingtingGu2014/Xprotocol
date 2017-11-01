/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.service;

import com.datastax.driver.core.utils.UUIDs;
import com.xprotocol.cassandra.model.UserProtocol;
import com.xprotocol.persistence.model.User;
import com.xprotocol.service.protocol.UserProtocolService;
import com.xprotocol.service.protocol.UserProtocolServiceImpl;
import com.xprotocol.service.user.UserService;
import com.xprotocol.utils.UtilsHelper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author Tao Zhao
 */

@SpringBootApplication
@PropertySource(value = { "application-xprotocol-persistence.properties" })
public class Application implements CommandLineRunner {
    
    @Autowired
    private UserService userSrv;
    
    @Autowired
    private UserProtocolService protocolSrv;
    
    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(Application.class)
                        .build()
                        .run(Application.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
//        List<User> users = userSrv.findAll();
//        users.forEach(x -> System.out.println(x));
//        UserProtocolService serv = new UserProtocolServiceImpl();
        UUID userProtocolUUID = UtilsHelper.getUUIDFromString("70b99490-bdff-11e7-abbd-8d88b42d3590");
//        UUID userUUID = UtilsHelper.getUUIDFromString("F3025AA06A4811E78574DCEF18A7FE04");
        UUID userUUID = UtilsHelper.getUUIDFromString("70b99491-bdff-11e7-abbd-8d88b42d3590");
        String title = "test title";
        String body = "test body";
        Set<UUID> projectUUIDs = new HashSet<UUID>();
        projectUUIDs.add(UUIDs.timeBased());
        Set<String> projectTitles = new HashSet<>();
        projectTitles.add("test project");
        Set<String> files = new HashSet<>();
        files.add("test file");
        Set<String> versions = new HashSet<>();
        versions.add("test version1");
        Set<String> keywords = new HashSet<>();
        keywords.add("biology");
        Map<String, String> comments = new HashMap<>();
        comments.put("tao-comments-2017-10-27", "I like this protocol");
        System.out.println("protocol UUID="+userProtocolUUID);
        System.out.println("user UUID="+userUUID);
        UserProtocol protocol = protocolSrv.findProtocolByUserUUIDAndProtocolUUID(userUUID, userProtocolUUID);
        System.out.println(protocol);
//        Iterable<UserProtocol> protocols = protocolSrv.findProtocolByUserUUID(userUUID);
//        protocols.forEach(System.out::println);
//        protocolSrv.createProtocol(userProtocolUUID, userUUID, title, body, projectUUIDs, projectTitles, files, comments, versions, keywords);
        System.out.println("done.");
    }
}
