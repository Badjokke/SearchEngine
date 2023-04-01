package com.example.ir_semestralka.utils;

import com.example.ir_semestralka.Constants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.logging.Level;

//class responsible for I/O operations
public class IOManager {
    private static final JSONParser jsonParser = new JSONParser();

    /**
     * create storage files if they dont exist
     * @param rewrite if this parameter is set to true, files will be rewritten even if they exist
     */
    public static boolean createStorage(boolean rewrite){
        File rootStorage = new File(Constants.storageRoot);
        boolean fileCreated = true;
        if(!rootStorage.exists() || rewrite) {
            Global.logger.log(Level.INFO,"Creating database: "+Constants.storageRoot+" on filesystem.");
            fileCreated = rootStorage.mkdir();
            if(!fileCreated){
                Global.logger.log(Level.SEVERE,"Failed to create database directory "+Constants.storageRoot);
                Global.logger.log(Level.INFO,"Killing the server.");
            }

        }
        return fileCreated;
    }

    public static JSONObject readJSONfile(String path){
        BufferedReader br = null;
        JSONObject jsonObject = null;
        try{
            br = new BufferedReader(new FileReader(path));
            jsonObject = (JSONObject) jsonParser.parse(br);

        } catch (FileNotFoundException e) {
            Global.logger.log(Level.WARNING,"Failed to load json file from path: "+ path);
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Global.logger.log(Level.WARNING,"Failed to load json file from path: "+ path);
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            Global.logger.log(Level.WARNING,"Failed to parse json file from path: "+ path);
            return null;
        }
        return (JSONObject) jsonObject.get("crawler");

    }





}
