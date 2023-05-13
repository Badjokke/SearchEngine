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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchEngineService implements SearchEngine {
    private final int PAGE_SIZE = 10;

    @Override
    public Map<Integer,List<Article>> retrieveDocuments(String query, VectorModel model, int page) {
        List<Integer> relevantArticleIds = SearchEngines.retrieveDocuments(query,model);
        if(relevantArticleIds == null)return null;
        final int n = relevantArticleIds.size();
        final int start = (page - 1) * PAGE_SIZE;
        final int stop = Math.min((start + PAGE_SIZE), n);
        final int pageCount = (int)Math.ceil((double)relevantArticleIds.size()/PAGE_SIZE);
        Map<Integer,List<Article>> pagedResult = new HashMap<>();

        List<Article> relevantArticles = new ArrayList<>();
        for(int i = start; i < stop; i++){
            int articleId = relevantArticleIds.get(i);
            JSONObject json = IOManager.readArticle(articleId);
            //article is no longer in the document cache
            if(json == null)continue;
            String author = (String) json.get("author");
            String title = (String)json.get("title");
            String content = (String)json.get("content");
            Article a = new ArticleBBC(author,title,getArticlePreview(content),articleId);
            relevantArticles.add(a);
        }
        pagedResult.put(pageCount,relevantArticles);
        return pagedResult;
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

    private String getArticlePreview(String articleContent){
        if(articleContent == null)
            return"No content";
        StringBuilder sb = new StringBuilder();
        String[] tmp = articleContent.split("\\s+");
        int end = Math.min(tmp.length,Constants.PAGE_SNIPPET_SIZE);
        for(int i = 0; i < end; i++)
            sb.append(tmp[i]).append(" ");
        return sb.toString();
    }


}
