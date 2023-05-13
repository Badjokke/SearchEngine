package com.example.ir_semestralka.global;

import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.VectorModel;
import com.example.ir_semestralka.search_engine.ISearchEngine;
import com.example.ir_semestralka.search_engine.SearchEngine;
import com.example.ir_semestralka.utils.Log;

import java.util.List;
import java.util.logging.Level;

public class SearchEngines {
    public static ISearchEngine tfIdfEngine;
    public static ISearchEngine bagOfWordsEngine;
    private static ISearchEngine booleanEngine = null;



    public static void initEngines(){
        booleanEngine = new SearchEngine(VectorModel.BINARY);
        /*if(load){
            booleanEngine.loadIndex();
            tfIdfEngine = new SearchEngine(VectorModel.TF_IDF);
            bagOfWordsEngine = new SearchEngine(VectorModel.BAG_OF_WORDS);
            tfIdfEngine.loadIndex();
            bagOfWordsEngine.loadIndex();
            return;
        }*/
        booleanEngine.createIndex();
        tfIdfEngine = new SearchEngine(booleanEngine.getCopyOfIndex(),VectorModel.TF_IDF);
        bagOfWordsEngine = new SearchEngine(booleanEngine.getCopyOfIndex(),VectorModel.BAG_OF_WORDS);

        bagOfWordsEngine.createIndex();
        tfIdfEngine.createIndex();
        //extremely slow for large index, meaningless
        //saveEngines();
    }
    //saves the inverted index data structure on filesystem
    //its extremely slow to save the disk on filesystem
    //not worth
    public static void saveEngines(){
        booleanEngine.saveIndex();
        bagOfWordsEngine.saveIndex();
        tfIdfEngine.saveIndex();
        Log.log(Level.INFO,"Engines saved");
    }
    private static void loadIndexes(){
        booleanEngine.loadIndex();
        bagOfWordsEngine.loadIndex();
        tfIdfEngine.loadIndex();
        Log.log(Level.INFO,"Engines loaded");
    }
    //add a new file to indexes
    //the method must be inside a monitor
    //multiple threads can try to access the shared resource - search engine instance
    public static synchronized void indexNewFile(Article article){
        booleanEngine.addNewDocumentToIndex(article);
        tfIdfEngine.addNewDocumentToIndex(article);
        bagOfWordsEngine.addNewDocumentToIndex(article);

    }

    public static List<Integer> retrieveDocuments(String query, VectorModel model, int page){
        switch (model){
            case BAG_OF_WORDS -> {
                return bagOfWordsEngine.retrieveDocuments(query,page);
            }
            case TF_IDF -> {
                return tfIdfEngine.retrieveDocuments(query,page);
            }
            case BINARY -> {
                return booleanEngine.retrieveDocuments(query, page);
            }

        }
        return null;
    }




}
