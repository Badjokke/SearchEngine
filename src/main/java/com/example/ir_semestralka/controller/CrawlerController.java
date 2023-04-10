package com.example.ir_semestralka.controller;

import com.example.ir_semestralka.service.ICrawler;
import com.example.ir_semestralka.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;

@RestController
public class CrawlerController {
    @Autowired
    private ICrawler crawler;


    @RequestMapping(value = "/crawler",method = RequestMethod.GET,produces = "application/json")
    public String scrapeNewBBCArticles(){
        Log.log(Level.INFO,"Crawler request recieved");
        crawler.crawlRootPages();




        return "{\"key\":\"jsem crawler a budu crawlit web\"}";

    }


}
