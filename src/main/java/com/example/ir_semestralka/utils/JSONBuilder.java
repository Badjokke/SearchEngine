package com.example.ir_semestralka.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSONBuilder {


    public String buildJSON(HashMap<String,Object> json){
        JSONObject jsonObject = new JSONObject();
        for(String key : json.keySet()){
            jsonObject.put(key,parseJSONValue(json.get(key)));
        }
        String jsonString = jsonObject.toJSONString();

        return jsonString;
    }
    private Object parseJSONValue(Object value){

        if(value instanceof HashMap<?,?>){
            Map<String,Object> map = null;
            try{
                map = (HashMap<String,Object>) value;
            }
            catch (ClassCastException e){
                throw new RuntimeException("Provided object of HashMap is not <String,Object> typed!");
            }
            JSONObject jsonObject = new JSONObject();
            for(String key : map.keySet())
                jsonObject.put(key,parseJSONValue(map.get(key)));
            return jsonObject;


        }
        if (value instanceof ArrayList){
            JSONArray jsonArray = new JSONArray();
            ArrayList<Object> list;
            list = (ArrayList<Object>) value;
            for(int i = 0,n=list.size(); i < n; i++)
                jsonArray.add(parseJSONValue(list.get(i)));
            return jsonArray;
        }


        //some simple data type without any indentation, we can just return it
        return value;
    }



}
