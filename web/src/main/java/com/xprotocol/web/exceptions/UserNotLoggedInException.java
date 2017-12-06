/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.web.exceptions;

/**
 *
 * @author zhao0677
 */
public class UserNotLoggedInException extends Exception {

    /**
     *
     * @param message
     */
    public UserNotLoggedInException(String message){
        super(message);
    }
}