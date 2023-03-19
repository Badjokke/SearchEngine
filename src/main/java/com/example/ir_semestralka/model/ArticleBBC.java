package com.example.ir_semestralka.model;

public class ArticleBBC{
    private String title, author, content;
    private static int id = 0;

    public ArticleBBC(String author, String title, String content){
        this.author = author;
        this.title = title;
        this.content = content;
        id++;
    }




    public String getTitle(){return this.title;}

    public String getAuthor(){return this.author;}

    public String getContent(){return this.content;}

    public int getId(){return id;}

}
