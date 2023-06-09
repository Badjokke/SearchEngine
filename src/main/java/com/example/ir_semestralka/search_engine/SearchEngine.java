package com.example.ir_semestralka.search_engine;

import com.example.ir_semestralka.global.Constants;
import com.example.ir_semestralka.index.IIndex;
import com.example.ir_semestralka.index.InvertedIndex;
import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.ArticleBBC;
import com.example.ir_semestralka.model.VectorModel;
import com.example.ir_semestralka.preprocessor.TextPreprocessor;
import com.example.ir_semestralka.utils.IOManager;
import com.example.ir_semestralka.utils.Log;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchEngine implements ISearchEngine {

    private final IIndex invertedIndex;
    private final QueryParser queryParser;
    private final VectorModel vectorModel;
    private Map<String,List<Integer>> queryCache;
    public SearchEngine(VectorModel vectorModel){
        this.invertedIndex = new InvertedIndex();
        this.queryParser = new DefaultQueryParser();
        this.vectorModel = vectorModel;
        this.queryCache = new HashMap<>();
    }
    public SearchEngine(IIndex index,VectorModel vectorModel){
        this.invertedIndex = index;
        this.queryParser = new DefaultQueryParser();
        this.vectorModel = vectorModel;
        this.queryCache = new HashMap<>();
    }


    @Override
    public void loadIndex(){
        invertedIndex.loadIndex(vectorModel);
    }

    @Override
    public void createIndex() {
        //index is not created - parse documents and create inverted index
        if(invertedIndex.isEmpty())
            createNewIndex();
        invertedIndex.convertToVectorModel(this.vectorModel);
        Log.log(Level.INFO,"Weighting performed");
    }

    private void createNewIndex(){
        File documentCache = new File(Constants.crawlerFileStorage);
        File[] articles = documentCache.listFiles();
        if(articles == null)return;
        int n = articles.length;
        for(int i = 0; i < n; i++){
            final String articleName = articles[i].getName();
            final String articlePath = articles[i].getAbsolutePath();
            JSONObject article = IOManager.readJSONfile(articlePath);
            Log.log(Level.INFO,"processing file "+ (i+1) + "out of "+n);
            if(article == null)continue;
            final String author = (String)article.get("author");
            final String title = (String)article.get("title");
            final String content = (String)article.get("content");
            int articleId = Integer.parseInt(articleName.split("_")[1].split("\\.")[0]);

            Article bbcArticle = new ArticleBBC(author,title,content,articleId);
            invertedIndex.indexDocument(bbcArticle);

        }

        Log.log(Level.INFO,"Inverted index created");

    }


    @Override
    public void saveIndex() {
        invertedIndex.saveIndex(vectorModel);
    }

    @Override
    public List<Integer> retrieveDocuments(String query) {
        if(query == null || query.length() == 0)return null;
        String tmp = query.trim();
        List<Integer> documents = null;
        if(this.queryCache.containsKey(tmp))
            documents = this.queryCache.get(tmp);
        else {
            documents = this.invertedIndex.retrieveDocuments(query,vectorModel);
            cacheQuery(tmp,documents);
        }
        return documents;

    }


    private void cacheQuery(String query, List<Integer> documentIds){
        //todo cache to db or filesystem for scale
        //scaling is not a big deal in school project
        this.queryCache.put(query,documentIds);
    }


    //returns deep copy of index
    @Override
    public IIndex getCopyOfIndex() {
        return this.invertedIndex.createDeepCopy();
    }
    //returns shallow copy of index
    @Override
    public IIndex getIndex() {
        return this.invertedIndex;
    }

    @Override
    public void addNewDocumentToIndex(Article article) {
        //invalidate the cache because query results might differ
        this.queryCache = new HashMap<>();
        this.invertedIndex.indexNewDocument(article,this.vectorModel);
    }


}
