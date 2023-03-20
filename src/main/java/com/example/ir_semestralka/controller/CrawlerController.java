package com.example.ir_semestralka.controller;

import com.example.ir_semestralka.utils.Global;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;

@RestController
public class CrawlerController {

    @RequestMapping(value = "/crawler",method = RequestMethod.GET,produces = "application/json")
    public String scrapeNewBBCArticles(){
        Global.logger.log(Level.INFO,"Crawler request recieved");
        return "{\"key\":\"jsem crawler a budu crawlit web\"}";
    }


}
