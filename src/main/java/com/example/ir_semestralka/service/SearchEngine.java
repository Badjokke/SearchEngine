package com.example.ir_semestralka.service;

import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.VectorModel;

import java.util.List;

public interface SearchEngine {

    List<Article> retrieveDocuments(String query, VectorModel model, int page);

    void indexArticle(int articleId);


}
