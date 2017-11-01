/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.cassandra;

import com.xprotocol.cassandra.repository.UserProtocolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zhao0677
 */
@SpringBootApplication
@PropertySource(value = { "application-xprotocol-cassandra.properties" })
public class Application implements CommandLineRunner {
    
    @Autowired
    private UserProtocolRepository repo;
    
    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(Application.class)
                        .build()
                        .run(Application.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
//        Set<String> test = new HashSet<>();
//        test.add("ok");
//        test.add("ok2");

        Map<String, String> test = new HashMap<>();
        test.put("1", "ok1");
        test.put("2", "ok2");
//        *** new user details ***
//        UserDetails details = new UserDetails(UUIDs.timeBased(), 2, "122 East AVE", "Moore", "OK", "45120");
//        repo.save(details);
          
//        *** find user details ***
//        UserDetails userDetails = (UserDetails) repo.findOne(id("userDetailsId", UUID.fromString("baad1580-58c0-11e7-b4f3-49c1dccc9187")));//repo.findAll();
//        repo.delete(UUID.fromString("baad1580-58c0-11e7-b4f3-49c1dccc9187"));
//        Iterable<UserDetails> userDetails = repo.findUserDetailsByUserDetailsId(UUID.fromString("baad1580-58c0-11e7-b4f3-49c1dccc9187"), 1);
//        userDetails.forEach(x -> System.out.println(x));

//        *** delete user details ***
//          repo.deleteByUserDetailsId(UUID.fromString("4069a8c0-59d2-11e7-b890-79a2af885cfe"));
//        List<UserDetails> userDetails = repo.findUserDetailsByUserId(1);
        System.out.println("set = "+test);
    }
}
