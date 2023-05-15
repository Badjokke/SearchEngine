package com.example.ir_semestralka.controller;

import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.service.FileStorageService;
import com.example.ir_semestralka.utils.JSONBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;


    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestBody MultipartFile file){
        Map<String,Object> json = new HashMap<>();
        if(file == null){
            json.put("message","no file sent in the request");
            return ResponseEntity.badRequest().body(JSONBuilder.buildJSON(json));
        }
        String filename = fileStorageService.storeFile(file);
        json.put("message",filename+" uploaded and indexed.");
        String jsonString = JSONBuilder.buildJSON(json);
        return ResponseEntity.ok(jsonString);
    }
    @GetMapping("/article")
    public ResponseEntity<String> showArticleContent(@RequestParam("id") int articleId){
        if(articleId == 0) return ResponseEntity.badRequest().body("invalid article id");
        Article a = fileStorageService.readArticle(articleId);
        String articleJson = a.toString();
        return ResponseEntity.ok(articleJson);
    }




}
