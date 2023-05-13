package com.example.ir_semestralka.utils;

import com.example.ir_semestralka.global.Constants;
import com.example.ir_semestralka.index.IIndex;
import com.example.ir_semestralka.index.InvertedIndex;
import com.example.ir_semestralka.index.PostingItem;
import com.example.ir_semestralka.model.VectorModel;
import com.example.ir_semestralka.preprocessor.TextPreprocessor;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class InvertedIndexUtils {
    public InvertedIndexUtils(){}
    public Map<String, List<PostingItem>> copyIndex(Map<String,List<PostingItem>> index){
        Set<String> keys = index.keySet();
        Map<String,List<PostingItem>> tmp = new HashMap<>();
        for(String key : keys){
            List<PostingItem> tmpItems = new ArrayList<>();
            List<PostingItem> indexItems = index.get(key);
            for(PostingItem item:indexItems){
                PostingItem tmpItem = new PostingItem(item.getArticleId(),item.getWeight());
                tmpItems.add(tmpItem);
            }
            tmp.put(key,tmpItems);
        }
        return tmp;

    }
    //method creates a deep copy (ie allocates new memory on heap and copies the values) of inverted index
    //its useful because we only have to create the inverted index once for every vector model
    //and then we just copy it - no need to process document collection again
    public IIndex createDeepCopy(Map<String, List<PostingItem>> titleIndex, Map<String, List<PostingItem>> contentIndex, Map<String, Integer> collectionTermFrequency, Map<Integer, Map<String, Integer>> documentTermFrequency) {
        //create a copy of indexes
        Map<String,List<PostingItem>> tmpTitleIndex = copyIndex(titleIndex);
        Map<String,List<PostingItem>> tmpContentIndex = copyIndex(contentIndex);
        Map<String,Integer> tmpCollectionTermFrequency = new HashMap<>();
        Map<Integer,Map<String,Integer>> tmpDocumentTermFrequency = new HashMap<>();
        Set<String> keys = collectionTermFrequency.keySet();
        //copy term frequency - very simple
        for(String key : keys)
            tmpCollectionTermFrequency.put(key,collectionTermFrequency.get(key));
        //copy document frequency - a bit more complicated because the structure is nested
        Set<Integer> documentIds = documentTermFrequency.keySet();
        for(Integer id : documentIds){
            Map<String,Integer> tmpDocFreq = new HashMap<>();
            tmpDocumentTermFrequency.put(id,tmpDocFreq);
            Map<String,Integer> docFreq = documentTermFrequency.get(id);
            keys = docFreq.keySet();
            for(String key : keys){
                tmpDocFreq.put(key,docFreq.get(key));
            }
        }
        return new InvertedIndex(tmpTitleIndex,tmpContentIndex,new TextPreprocessor(),tmpCollectionTermFrequency,tmpDocumentTermFrequency);
    }

    public double calculateTfIdf(int termFrequency, int documentFrequency, int documentCollectionSize){
        if(termFrequency == 0)return 0.0;
        double tf = 1 + Math.log10(termFrequency);
        double idf = Math.log10((double)documentCollectionSize/documentFrequency);
        return tf * idf;

    }
    // io operations take extremely long time for big (100MB+) index size
    // its faster to recreate the index in memory
    public void saveIndex(Map<String, List<PostingItem>> titleIndex, Map<String, List<PostingItem>> contentIndex, Map<String, Integer> collectionTermFrequency, Map<Integer, Map<String, Integer>> documentTermFrequency, Map<Integer,Double> documentSize, VectorModel model) {

        String modelPathPrefix = "";
        switch (model){
            case BAG_OF_WORDS -> modelPathPrefix = "/bag_of_words/";
            case BINARY -> modelPathPrefix = "/binary/";
            case TF_IDF -> modelPathPrefix = "/tf_idf/";

        }
        File f = new File(Constants.indexRoot+modelPathPrefix);
        if(!f.exists())
            f.mkdirs();
        try{
            //save the title index

            FileOutputStream fs = new FileOutputStream(Constants.indexRoot+modelPathPrefix+Constants.titleIndexPath);
            ObjectOutputStream outputStream = new ObjectOutputStream(fs);
            outputStream.writeObject(titleIndex);
            outputStream.close();
            fs.close();

            fs = new FileOutputStream(Constants.indexRoot+modelPathPrefix+Constants.contentIndexPath);
            outputStream = new ObjectOutputStream(fs);
            outputStream.writeObject(contentIndex);
            outputStream.close();
            fs.close();

            fs = new FileOutputStream(Constants.indexRoot+modelPathPrefix+Constants.collectionTermFrequencyPath);
            outputStream = new ObjectOutputStream(fs);
            outputStream.writeObject(collectionTermFrequency);
            outputStream.close();
            fs.close();

            fs = new FileOutputStream(Constants.indexRoot+modelPathPrefix+Constants.documentTermFrequencyPath);
            outputStream = new ObjectOutputStream(fs);
            outputStream.writeObject(documentTermFrequency);
            outputStream.close();
            fs.close();

            fs = new FileOutputStream(Constants.indexRoot+modelPathPrefix+Constants.documentSizePath);
            outputStream = new ObjectOutputStream(fs);
            outputStream.writeObject(documentSize);
            outputStream.close();
            fs.close();


        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadIndex(Map<String, List<PostingItem>> titleIndex, Map<String, List<PostingItem>> contentIndex, Map<String, Integer> collectionTermFrequency, Map<Integer, Map<String, Integer>> documentTermFrequency,Map<Integer,Double> documentSize,VectorModel model) {
        File f = new File(Constants.indexRoot);
        if(!f.exists()){
            Log.log(Level.SEVERE,"Inverted index is not saved on the disk. Running away.");
            return;
        }
        String modelPathPrefix = "";
        switch (model){
            case BAG_OF_WORDS -> modelPathPrefix = "bag_of_words/";
            case BINARY -> modelPathPrefix = "binary/";
            case TF_IDF -> modelPathPrefix = "tf_idf/";
        }
        try{
            //load  title index
            FileInputStream fs = new FileInputStream(modelPathPrefix+Constants.titleIndexPath);
            ObjectInputStream inputStream = new ObjectInputStream(fs);
            Object o = inputStream.readObject();
            titleIndex = (Map<String,List<PostingItem>>)o;
            fs.close();

            fs = new FileInputStream(modelPathPrefix+Constants.contentIndexPath);
            inputStream = new ObjectInputStream(fs);
            o = inputStream.readObject();
            contentIndex = (Map<String,List<PostingItem>>)o;
            fs.close();

            fs = new FileInputStream(modelPathPrefix+Constants.collectionTermFrequencyPath);
            inputStream = new ObjectInputStream(fs);
            o = inputStream.readObject();
            collectionTermFrequency = (Map<String,Integer>)o;
            fs.close();
            fs = new FileInputStream(modelPathPrefix+Constants.documentTermFrequencyPath);
            inputStream = new ObjectInputStream(fs);
            o = inputStream.readObject();
            documentTermFrequency = (Map<Integer, Map<String, Integer>>)o;
            fs.close();

            fs = new FileInputStream(modelPathPrefix+Constants.documentSizePath);
            inputStream = new ObjectInputStream(fs);
            o = inputStream.readObject();
            documentSize = (Map<Integer,Double>)o;
            fs.close();

        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
