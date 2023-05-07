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
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchEngine implements ISearchEngine {

    private final IIndex invertedIndex;
    private final QueryParser queryParser;
    private final VectorModel vectorModel;
    public SearchEngine(VectorModel vectorModel){
        this.invertedIndex = new InvertedIndex();
        this.queryParser = new DefaultQueryParser();
        this.vectorModel = vectorModel;
    }
    public SearchEngine(IIndex index,VectorModel vectorModel){
        this.invertedIndex = index;
        this.queryParser = new DefaultQueryParser();
        this.vectorModel = vectorModel;

    }


    @Override
    public void loadIndex(){
        invertedIndex.loadIndex();
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
        invertedIndex.saveIndex();
    }

    @Override
    public List<Integer> retrieveDocuments(String query) {
        if(query == null)return null;
        return this.invertedIndex.retrieveDocuments(query,vectorModel);

    }
    //TODO deep copy
    @Override
    public IIndex getIndex() {
        return this.invertedIndex.createDeepCopy();
    }

}
