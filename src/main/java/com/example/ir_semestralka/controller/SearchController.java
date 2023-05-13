package com.example.ir_semestralka.controller;
import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.VectorModel;
import com.example.ir_semestralka.service.SearchEngine;
import com.example.ir_semestralka.utils.JSONBuilder;
import com.example.ir_semestralka.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Level;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {
    @Autowired
    SearchEngine searchEngineService;
    @RequestMapping(method = RequestMethod.GET,value = "/search",produces = "application/json")
    public ResponseEntity<String> searchQuery(@RequestParam String query, @RequestParam(defaultValue = "1") int vectorModel, @RequestParam(defaultValue = "1") int page){
        Map<String,Object> jsonObject = new HashMap<>();
        if(query == null || query.length() == 0){
            jsonObject.put("message","no query provided");
            return ResponseEntity.badRequest().body(JSONBuilder.buildJSON(jsonObject));
        }
        if(page == 0){
            jsonObject.put("message","invalid page number");
            return ResponseEntity.badRequest().body(JSONBuilder.buildJSON(jsonObject));
        }
        VectorModel model;
        switch (vectorModel){
            case 1 ->model = VectorModel.TF_IDF;
            case 2 -> model = VectorModel.BAG_OF_WORDS;
            default -> model = VectorModel.BINARY;
        }
        Map<Integer,List<Article>> pagedResult = searchEngineService.retrieveDocuments(query,model,page);
        //key of the map is the number of pages user can browse
        int pageCount = (int) pagedResult.keySet().toArray()[0];
        List<Article> retrievedArticles = pagedResult.get(pageCount);
        Map<String,Object> tmp = new HashMap<>();
        tmp.put("page_count",pageCount);
        tmp.put("articles",retrievedArticles);
        String jsonBody  = JSONBuilder.buildJSON(tmp);
       return ResponseEntity.ok(jsonBody);
    }

}
