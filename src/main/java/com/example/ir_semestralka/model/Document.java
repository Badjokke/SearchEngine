package com.example.ir_semestralka.model;

public class Document{

    private double relevance;
    private int id;

    public Document(int id, double relevance){
        this.relevance = relevance;
        this.id = id;
    }
    public void setRelevance(double relevance){
        this.relevance = relevance;
    }
    public double getRelevance(){
        return this.relevance;
    }
    public int getId(){
        return this.id;
    }

}
