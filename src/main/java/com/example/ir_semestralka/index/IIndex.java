package com.example.ir_semestralka.index;

import com.example.ir_semestralka.model.Article;

import java.util.List;

public interface IIndex {
    boolean addToIndex(String term, List<String> postingList);
    void saveIndex();
    void loadIndex();
    void indexDocument(Article article);

}
