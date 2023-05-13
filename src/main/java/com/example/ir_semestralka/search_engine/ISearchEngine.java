package com.example.ir_semestralka.search_engine;

import com.example.ir_semestralka.index.IIndex;
import com.example.ir_semestralka.model.Article;

import java.util.List;

public interface ISearchEngine {
    void loadIndex();
    void createIndex();
    void saveIndex();
    List<Integer> retrieveDocuments(String query,int page);
    IIndex getCopyOfIndex();
    IIndex getIndex();

    void addNewDocumentToIndex(Article article);
}
