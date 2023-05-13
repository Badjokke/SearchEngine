package com.example.ir_semestralka.service;

import com.example.ir_semestralka.global.Constants;
import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.ArticleBBC;
import com.example.ir_semestralka.utils.IOManager;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorage implements FileStorageService {
    @Autowired
    SearchEngine searchEngineService;
    @Override
    public String storeFile(MultipartFile file) {
        Path uploadPath = Paths.get(Constants.crawlerFileStorage);

        String filename = file.getOriginalFilename();
        if(filename == null)
            return null;
        int articleId = IOManager.getArticleCount() + 1;
        //store the file name in format suitable for server
        String articleFilename = "article_"+articleId+".json";
        try{
            InputStream inputStream = file.getInputStream();
            Path targetLocation = uploadPath.resolve(articleFilename);
            File destFile = targetLocation.toFile();
            OutputStreamWriter outputStream = new OutputStreamWriter(new FileOutputStream(destFile));
            int val;

            while((val = inputStream.read()) != -1 )
                outputStream.write(val);

            outputStream.flush();
            inputStream.close();
            outputStream.close();
        }
        catch (IOException exception){
            exception.printStackTrace();
            return null;
        }
        searchEngineService.indexArticle(articleId);
        //return the original filename for the user
        return filename;

    }

    @Override
    public Article readArticle(int articleId) {
        JSONObject json = IOManager.readArticle(articleId);
        String author = (String) json.get("author");
        String title = (String)json.get("title");
        String content = (String)json.get("content");
        Article a = new ArticleBBC(author,title,content,articleId);
        return a;
    }
}
