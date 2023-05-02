package com.example.ir_semestralka.search_engine;

import com.example.ir_semestralka.global.Constants;
import com.example.ir_semestralka.index.IIndex;
import com.example.ir_semestralka.index.InvertedIndex;
import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.ArticleBBC;
import com.example.ir_semestralka.model.VectorModel;
import com.example.ir_semestralka.utils.IOManager;
import com.example.ir_semestralka.utils.Log;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public class SearchEngine implements ISearchEngine {

    private final IIndex invertedIndex;
    public SearchEngine(){
        this.invertedIndex = new InvertedIndex();
    }
    public SearchEngine(IIndex index){
        this.invertedIndex = index;
    }


    @Override
    public void loadIndex(){
        invertedIndex.loadIndex();
    }

    @Override
    public void createIndex(VectorModel model) {
        //index is not created - parse documents and create inverted index
        if(invertedIndex.isEmpty())
            createNewIndex();
        invertedIndex.convertToVectorModel(model);
        Log.log(Level.INFO,"Weighting performed");
    }


    private void createNewIndex(){

        File documentCache = new File(Constants.crawlerFileStorage);
        File[] articles = documentCache.listFiles();
        if(articles == null)return;
        int n = articles.length;
        for(int i = 0; i < n; i++){
            final String articleName = articles[i].getAbsolutePath();
            JSONObject article = IOManager.readJSONfile(articleName);
            Log.log(Level.INFO,"processing file "+ (i+1) + "out of "+n);
            if(article == null)continue;
            final String author = (String)article.get("author");
            final String title = (String)article.get("title");
            final String content = (String)article.get("content");

            Article bbcArticle = new ArticleBBC(author,title,content,i+1);
            invertedIndex.indexDocument(bbcArticle);
        }

        Log.log(Level.INFO,"Inverted index created");

    }


    @Override
    public void saveIndex() {
        invertedIndex.saveIndex();
    }

    @Override
    public List<Integer> retrieveDocuments(String query) {
        return null;
    }
    //TODO deep copy
    @Override
    public IIndex getIndex() {
        return this.invertedIndex.createDeepCopy();
    }

}
