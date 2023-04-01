package com.example.ir_semestralka.utils;

import com.example.ir_semestralka.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CrawlerUtil {

    private JSONObject config;
    private int crawlingDepth;
    private int politeness;
    private List<String> xPaths;
    private String rootPage;
    private String page_subcategory;

    public CrawlerUtil(){
        //initialize default values of crawler
        this.crawlingDepth = 1;
        this.politeness = 1000;
        this.xPaths = null;
        this.rootPage = "https://www.bbc.com";
        this.page_subcategory = null;

        config = IOManager.readJSONfile(Constants.CRAWLER_CONFIG_PATH);
        this.parseConfigFile();
    }
    private void parseConfigFile(){
        if(this.config == null)return;
        Object tmp = null;
        tmp =  config.get("crawling_depth");

        if(tmp != null)
            this.crawlingDepth =  Integer.parseInt((String) tmp);
        tmp = config.get("politeness");

        if(tmp != null)
            this.politeness = Integer.parseInt((String) tmp);
        tmp = config.get("xpaths");

        if(tmp != null){
            this.xPaths = new ArrayList<>();
            Iterator arrayIterator = ((JSONArray) tmp).iterator();
            while(arrayIterator.hasNext()){
                this.xPaths.add((String) arrayIterator.next());
            }

        }
        tmp = config.get("root_page");
        if(tmp != null)
            this.rootPage = (String) tmp;

        tmp = config.get("page_subcategory");
        if(tmp != null)
            this.page_subcategory = (String) tmp;

    }

}
