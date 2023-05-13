package com.example.ir_semestralka.service;

import com.example.ir_semestralka.global.Constants;
import com.example.ir_semestralka.global.SearchEngines;
import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.ArticleBBC;
import com.example.ir_semestralka.model.VectorModel;
import com.example.ir_semestralka.utils.IOManager;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchEngineService implements SearchEngine {
    @Override
    public List<Article> retrieveDocuments(String query, VectorModel model, int page) {
        List<Integer> relevantArticleIds = SearchEngines.retrieveDocuments(query,model, page);
        if(relevantArticleIds == null)return null;
        List<Article> relevantArticles = new ArrayList<>();
        for(int articleId : relevantArticleIds){
            JSONObject json = IOManager.readArticle(articleId);
            //article is no longer in the document cache
            if(json == null)continue;
            String author = (String) json.get("author");
            String title = (String)json.get("title");
            String content = (String)json.get("content");
            Article a = new ArticleBBC(author,title,content.substring(0, Constants.PAGE_SNIPPET_SIZE),articleId);
            relevantArticles.add(a);
        }
        return relevantArticles;
    }

    @Override
    public void indexArticle(int articleId) {
        JSONObject json = IOManager.readArticle(articleId);
        String author = (String) json.get("author");
        String title = (String)json.get("title");
        String content = (String)json.get("content");
        Article a = new ArticleBBC(author,title,content,articleId);
        SearchEngines.indexNewFile(a);
    }



}
