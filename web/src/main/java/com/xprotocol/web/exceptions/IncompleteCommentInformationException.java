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
public class IncompleteCommentInformationException extends Exception {

    /**
     *
     * @param message
     */
    public IncompleteCommentInformationException(String message){
        super(message);
    }
}