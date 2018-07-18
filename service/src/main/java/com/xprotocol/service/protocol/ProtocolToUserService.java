/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.service.protocol;

import com.xprotocol.cassandra.model.ProtocolToUser;
import java.util.List;

/**
 *
 * @author zhao0677
 */
public interface ProtocolToUserService {
    public List<ProtocolToUser> findProtocolToUserListByLimit(int limit);
}
