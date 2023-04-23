package com.example.ir_semestralka.search_engine;

import java.util.List;

public interface ISearchEngine {
    void loadIndex();
    void createIndex();
    void saveIndex();
    List<Integer> retrieveDocuments(String query);

}
