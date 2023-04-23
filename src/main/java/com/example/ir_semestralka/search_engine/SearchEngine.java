package com.example.ir_semestralka.search_engine;

import com.example.ir_semestralka.global.Constants;
import com.example.ir_semestralka.index.IIndex;
import com.example.ir_semestralka.index.InvertedIndex;
import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.ArticleBBC;
import com.example.ir_semestralka.utils.IOManager;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.List;
public class SearchEngine implements ISearchEngine {

    private final IIndex invertedIndex;
    public SearchEngine(){
        this.invertedIndex = new InvertedIndex();
    }



    @Override
    public void loadIndex(){
        invertedIndex.loadIndex();
    }

    @Override
    public void createIndex() {
        File documentCache = new File(Constants.crawlerFileStorage);
        File[] articles = documentCache.listFiles();
        if(articles == null)return;

        for(int i = 0; i < articles.length; i++){
            final String articleName = articles[i].getAbsolutePath();
            JSONObject article = IOManager.readJSONfile(articleName);
            System.out.println(article);
            if(article == null)continue;
            final String author = (String)article.get("author");
            final String title = (String)article.get("title");
            final String content = (String)article.get("content");

            Article bbcArticle = new ArticleBBC(author,title,content);
            invertedIndex.indexDocument(bbcArticle);
        }


    }

    @Override
    public void saveIndex() {
        invertedIndex.saveIndex();
    }

    @Override
    public List<Integer> retrieveDocuments(String query) {
        return null;
    }

}
