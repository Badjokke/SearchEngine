package com.example.ir_semestralka;

import com.example.ir_semestralka.search_engine.SearchEngine;
import com.example.ir_semestralka.utils.CrawlerUtil;
import com.example.ir_semestralka.utils.IOManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IrSemestralkaApplication {

    public static void main(String[] args) {
        //if creating storage failed there is no reason to even start the app
        if(!IOManager.createStorage(false))return;
        if(!IOManager.createDocumentStorage(false))return;
        //load configuration for crawler
        CrawlerUtil.loadConfigFile();
        SearchEngine engine = new SearchEngine();
        engine.createIndex();
        SpringApplication.run(IrSemestralkaApplication.class, args);
    }

}
