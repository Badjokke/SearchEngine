package com.example.ir_semestralka.index;

import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.VectorModel;

import java.util.List;

public interface IIndex {
    void saveIndex(VectorModel model);
    void loadIndex(VectorModel model);
    void indexDocument(Article article);
    boolean isEmpty();
    void convertToVectorModel(VectorModel model);

    double calculateTfIdf(int termFrequency, int documentFrequency, int documentCollectionSize);
    int calculateTermFreq(String term);

    int getCollectionSize();

    IIndex createDeepCopy();

    List<Integer> retrieveDocuments(String query,VectorModel vectorModel);

    void indexNewDocument(Article article, VectorModel vectorModel);


}
