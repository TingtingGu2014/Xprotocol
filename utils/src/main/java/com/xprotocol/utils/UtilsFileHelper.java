/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.utils;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author zhao0677
 */
public class UtilsFileHelper {
    public static void moveFile(String source, String target) throws IOException {
        File sourceFile = new File(source);
        sourceFile.renameTo(new File(target + File.separator + sourceFile.getName()));
    }
}
