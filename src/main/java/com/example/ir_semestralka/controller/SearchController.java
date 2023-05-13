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
        VectorModel model;
        switch (vectorModel){
            case 1 ->model = VectorModel.TF_IDF;
            case 2 -> model = VectorModel.BAG_OF_WORDS;
            default -> model = VectorModel.BINARY;
        }
        List<Article> retrievedArticles = searchEngineService.retrieveDocuments(query,model,page);
        String jsonBody = generateArticleJson(retrievedArticles);

       return ResponseEntity.ok(jsonBody);
    }



    private String generateArticleJson(List<Article> articles){
        Map<String,Object> json = new HashMap<>();
        json.put("articles",articles);
        String jsonString = JSONBuilder.buildJSON(json);
        return jsonString;
    }

}
