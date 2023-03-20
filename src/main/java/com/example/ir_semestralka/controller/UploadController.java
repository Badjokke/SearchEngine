package com.example.ir_semestralka.controller;

import com.example.ir_semestralka.utils.Global;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.logging.Level;

//TODO napsat tridu obalujici upload request pro automatickou serializaci
@RestController
public class UploadController {
@RequestMapping(produces = "application/json",method = RequestMethod.GET,value = "/upload")
    public String uploadDocument(@RequestParam Map<String,String> parameters){
    Global.logger.log(Level.INFO,"Upload request recieved");
    return "{\"key\":\"jsem upload stranka a budu uploadit a indexovat vas soubor!\"}";
}

}
