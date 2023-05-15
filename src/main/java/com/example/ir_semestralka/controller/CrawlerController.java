package com.example.ir_semestralka.controller;

import com.example.ir_semestralka.service.ICrawler;
import com.example.ir_semestralka.utils.CrawlerUtil;
import com.example.ir_semestralka.utils.JSONBuilder;
import com.example.ir_semestralka.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CrawlerController {
    @Autowired
    private ICrawler crawler;


    @RequestMapping(value = "/crawler",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<String> scrapeNewBBCArticles(){
        if(CrawlerUtil.isActive()){
            Map<String,Object> json = new HashMap<>();
            json.put("message","crawling is already in progress");
            return ResponseEntity.badRequest().body(JSONBuilder.buildJSON(json));
        }
        Log.log(Level.INFO,"Crawler request recieved");
        crawler.crawlRootPages();
        Map<String,Object> json = new HashMap<>();
        json.put("message","crawling finished");
        return ResponseEntity.ok(JSONBuilder.buildJSON(json));
    }
    @PostMapping(value="/crawl_page")
    public ResponseEntity<String> crawlBBCArticle(@RequestBody Map<String,String> body){
        Map<String,Object> json = new HashMap<>();
        if(body == null || body.size() == 0 || !body.containsKey("url")){
            json.put("message","no url provided");
            return ResponseEntity.badRequest().body(JSONBuilder.buildJSON(json));
        }
        String url = body.get("url");
        boolean crawled = crawler.crawlPage(url);
        if(!crawled){
            json.put("message","invalid url: "+url+" make sure the article is from bbc news");
            return ResponseEntity.badRequest().body(JSONBuilder.buildJSON(json));
        }
        json.put("message",url + " parsed.");
        return ResponseEntity.ok(JSONBuilder.buildJSON(json));
    }



}
