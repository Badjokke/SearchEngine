package com.example.ir_semestralka.index;

public class PostingItem {
    private int articleId;
    private double weight;


    public int getArticleId() {return this.articleId;}
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public PostingItem(int articleId, double weight){
           this.articleId = articleId;
           this.weight = weight;
    }


}
