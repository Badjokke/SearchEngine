package com.example.ir_semestralka.controller;

import org.springframework.web.bind.annotation.*;

//TODO pouze admin account muze crawlit
@RestController
public class CrawlerController {

    @RequestMapping(value = "/crawler",method = RequestMethod.POST)
    public String scrapeNewBBCArticles(){
        return "{\"key\":\"helloworld\"}";
    }


}
