/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.service.exceptions;

/**
 *
 * @author zhao0677
 */
public class EntityDoesNotExistException extends Exception {

    /**
     *
     * @param message
     */
    public EntityDoesNotExistException(String message){
        super(message);
    }
    
}
