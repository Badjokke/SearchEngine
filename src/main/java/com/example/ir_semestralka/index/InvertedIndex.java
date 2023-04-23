package com.example.ir_semestralka.index;

import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.preprocessor.TextPreprocessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex implements IIndex{
    private final Map<String,List<String>> titleIndex;
    private final Map<String,List<String>> contentIndex;
    private final TextPreprocessor preprocessor;
    private final Map<String,Integer> termFrequency;
    public InvertedIndex(){
        this.titleIndex = new HashMap<>();
        this.contentIndex = new HashMap<>();
        this.preprocessor = new TextPreprocessor();
        this.termFrequency = new HashMap<>();


    }



    @Override
    public boolean addToIndex(String term, List<String> postingList) {
        return false;
    }

    @Override
    public void saveIndex() {

    }

    @Override
    public void loadIndex() {

    }
    private void processText(List<String> list){

    }


    @Override
    public void indexDocument(Article article) {
        final int articleId = article.getId();
        final String articleAuthor = article.getAuthor();
        final String articleTitle = article.getTitle();
        final String articleContent = article.getContent();

        List<String> authorPreprocessed = this.preprocessor.getTokens(articleAuthor);
        List<String> titlePreprocessed = this.preprocessor.getTokens(articleTitle);
        List<String> articlePreprocessed = this.preprocessor.getTokens(articleContent);
        processText(authorPreprocessed);
        processText(articlePreprocessed);
        processText(titlePreprocessed);

        System.out.println("xyz");


    }

}
