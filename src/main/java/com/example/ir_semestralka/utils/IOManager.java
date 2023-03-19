package com.example.ir_semestralka.utils;

import com.example.ir_semestralka.Constants;

import java.io.File;
import java.util.logging.Level;

//class responsible for I/O operations
public class IOManager {

    /**
     * create storage files if they dont exist
     * @param rewrite if this parameter is set to true, files will be rewritten even if they exist
     */
    public static void createStorage(boolean rewrite){
        File rootStorage = new File(Constants.storageRoot);
        boolean fileCreated = false;
        if(!rootStorage.exists() || rewrite) {
            fileCreated = rootStorage.mkdir();
            if(!fileCreated){
                Global.logger.log(Level.SEVERE,"Failed to create database directory "+Constants.storageRoot);
                Global.logger.log(Level.INFO,"Killing the server.");

                System.exit(1);
            }

        }


    }





}
