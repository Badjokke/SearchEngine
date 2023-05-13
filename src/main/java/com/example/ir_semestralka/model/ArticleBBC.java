package com.example.ir_semestralka.model;

import com.example.ir_semestralka.utils.JSONBuilder;

import java.util.HashMap;
import java.util.Map;

public class ArticleBBC implements Article{
    private final String title, author, content;
    private final int id;
    public ArticleBBC(String author, String title, String content, int id){
        this.author = author;
        this.title = title;
        this.content = content;
        this.id = id;
    }




    public String getTitle(){return this.title;}

    public String getAuthor(){return this.author;}

    public String getContent(){return this.content;}

    public int getId(){return id;}
    @Override
    public String toString(){
        Map<String,Object> json = new HashMap<>();
        json.put("article_id",getId());
        json.put("author",getAuthor());
        json.put("title",getTitle());
        json.put("content",getContent());
        return JSONBuilder.buildJSON(json);
    }

}
