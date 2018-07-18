/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.service.protocol;

import com.xprotocol.cassandra.model.ProtocolToUser;
import com.xprotocol.cassandra.repository.ProtocolToUserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhao0677
 */
@Service
@ComponentScan("com.xprotocol.cassandra")
public class ProtocolToUserServiceImpl implements ProtocolToUserService {
    
    @Autowired
    ProtocolToUserRepository protocolToUserRepo;

    @Override
    public List<ProtocolToUser> findProtocolToUserListByLimit(int limit) {
        return protocolToUserRepo.findProtocolToUserListByLimit(limit);        
    }
    
}
