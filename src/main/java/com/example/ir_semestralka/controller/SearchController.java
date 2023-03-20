package com.example.ir_semestralka.controller;

import com.example.ir_semestralka.utils.Global;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Level;

//todo napsat tridu obalujici query request pro automatickou serializaci a pohodlnost
@RestController
public class SearchController {
    @RequestMapping(method = RequestMethod.GET,value = "/search",produces = "application/json")
    public String searchQuery(@RequestParam Optional<String> query){
        Global.logger.log(Level.INFO,"Query search recieved");
        return "{\"key\":\"jsem search query a searchuju :"+query.orElseGet(()->"query nebylo poskytnuto")+"\"}";
    }

}
