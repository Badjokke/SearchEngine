package com.example.ir_semestralka.global;

import com.example.ir_semestralka.model.VectorModel;
import com.example.ir_semestralka.search_engine.ISearchEngine;
import com.example.ir_semestralka.search_engine.SearchEngine;

public class SearchEngines {
    public static ISearchEngine tfIdfEngine;
    public static ISearchEngine bagOfWordsEngine;
    public static ISearchEngine booleanEngine;



    public static void initEngines(){
        booleanEngine = new SearchEngine(VectorModel.BINARY);
        booleanEngine.createIndex();
        tfIdfEngine = new SearchEngine(booleanEngine.getIndex(),VectorModel.TF_IDF);
        bagOfWordsEngine = new SearchEngine(booleanEngine.getIndex(),VectorModel.BAG_OF_WORDS);

        bagOfWordsEngine.createIndex();
        tfIdfEngine.createIndex();
    }
}
