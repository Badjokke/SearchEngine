package com.example.ir_semestralka;

import com.example.ir_semestralka.global.SearchEngines;
import com.example.ir_semestralka.utils.CrawlerUtil;
import com.example.ir_semestralka.utils.IOManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IrSemestralkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(IrSemestralkaApplication.class, args);

        //if creating storage failed there is no reason to even start the app
        if(!IOManager.createStorage(false))return;
        if(!IOManager.createDocumentStorage(false))return;
        //load configuration for crawler
        CrawlerUtil.loadConfigFile();

        SearchEngines.initEngines();

        SearchEngines.tfIdfEngine.retrieveDocuments("arachnocetnric king queen");

    }

}
