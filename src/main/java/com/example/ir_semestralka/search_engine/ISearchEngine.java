package com.example.ir_semestralka.search_engine;

import com.example.ir_semestralka.index.IIndex;
import com.example.ir_semestralka.model.VectorModel;

import java.util.List;

public interface ISearchEngine {
    void loadIndex();
    void createIndex(VectorModel model);
    void saveIndex();
    List<Integer> retrieveDocuments(String query);
    IIndex getIndex();

}
