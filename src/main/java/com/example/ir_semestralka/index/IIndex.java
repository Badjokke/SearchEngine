package com.example.ir_semestralka.index;

import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.VectorModel;

import java.util.PriorityQueue;

public interface IIndex {
    void saveIndex();
    void loadIndex();
    void indexDocument(Article article);
    boolean isEmpty();
    void convertToVectorModel(VectorModel model);

    double calculateTfIdf(int termFrequency, int documentFrequency, int documentCollectionSize);
    int calculateTermFreq(String term);

    int getCollectionSize();

    IIndex createDeepCopy();

}
