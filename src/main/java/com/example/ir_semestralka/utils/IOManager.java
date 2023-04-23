package com.example.ir_semestralka.utils;

import com.example.ir_semestralka.global.Constants;
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
        boolean fileCreated = false;

        try{
            fileCreated = createFile(rewrite,Constants.storageRoot);

        }
        catch (IOException exception){
            Log.log(Level.SEVERE,"Failed to create  storage directory "+Constants.crawlerFileStorage);
            exception.printStackTrace();
            return false;

        }
        return fileCreated;
    }

    /**
     *
     * @param rewrite if this flag is set to true, file will be rewritten
     * @return true if file was created
     */
    public static boolean createDocumentStorage(boolean rewrite){
        boolean fileCreated = false;
        try{
            fileCreated = createFile(rewrite,Constants.crawlerFileStorage);
        }
        catch (IOException exception){
            Log.log(Level.SEVERE,"Failed to create crawler storage file "+Constants.crawlerFileStorage);
            exception.printStackTrace();
            return false;
        }
        return fileCreated;
    }

    public static boolean writeJSONfile(String content, String path) throws IOException {
        //createFile(true,path);
        FileWriter file = new FileWriter(path);
        file.write(content);
        file.flush();
        file.close();
        return true;
    }

    private static boolean createFile(boolean rewrite, String path) throws IOException{
        File rootStorage = new File(path);
        boolean fileCreated = true;
        if(!rootStorage.exists() || rewrite) {
            //Global.logger.log(Level.INFO,"Creating file: "+path+" on filesystem.");
            fileCreated = rootStorage.mkdir();
            if(!fileCreated){
                Log.log(Level.SEVERE,"Failed to create file "+ path);
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
            Log.log(Level.WARNING,"Failed to load json file from path: "+ path);
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.log(Level.WARNING,"Failed to load json file from path: "+ path);
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.log(Level.WARNING,"Failed to parse json file from path: "+ path);
            return null;
        }
        return jsonObject;

    }


    public static String[] loadFileContent(String path, String delimiter) {
        File file = new File(path);
        if(!file.exists())return null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try{
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = br.readLine())!=null)
                sb.append(line).append(delimiter);
        }
        catch (IOException exception){
            Log.log(Level.WARNING,"File not found at: "+path);
        }
        String content = sb.toString();
        return content.split(delimiter);
    }
}
