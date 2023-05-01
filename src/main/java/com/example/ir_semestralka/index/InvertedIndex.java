package com.example.ir_semestralka.index;

import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.VectorModel;
import com.example.ir_semestralka.preprocessor.TextPreprocessor;
import com.example.ir_semestralka.utils.Log;

import java.util.*;
import java.util.logging.Level;

public class InvertedIndex implements IIndex{
    private final Map<String,List<PostingItem>> titleIndex;
    private final Map<String,List<PostingItem>> contentIndex;
    private final TextPreprocessor preprocessor;
    private final Map<String,Integer> collectionTermFrequency;

    private final Map<Integer,Map<String,Integer>> documentTermFrequency;

    public InvertedIndex(){
        this.titleIndex = new HashMap<>();
        this.contentIndex = new HashMap<>();
        this.preprocessor = new TextPreprocessor();
        this.collectionTermFrequency = new HashMap<>();
        this.documentTermFrequency = new HashMap<>();
    }




    @Override
    public void saveIndex() {

    }

    @Override
    public void loadIndex() {

    }

    private void processText(List<String> list, int articleId, Map<String,List<PostingItem>> index){
        //need to save terms that are already in the posting list so we dont add redundant information
        Set<String> visitedTerms = new HashSet<>();
        for(String token : list){
            incrementTermFrequency(token);
            incrementDocumentTermFrequency(articleId,token);
            //already counter for, we dont want to add the same posting item twice
            if(visitedTerms.contains(token))continue;
            if(!index.containsKey(token))
                index.put(token,new ArrayList<>());
            List<PostingItem> postingList = index.get(token);
            visitedTerms.add(token);
            postingList.add(new PostingItem(articleId,1));
        }
    }

    private void incrementTermFrequency(String token){
        if(!this.collectionTermFrequency.containsKey(token))
            this.collectionTermFrequency.put(token,0);
        int count = this.collectionTermFrequency.get(token);
        this.collectionTermFrequency.put(token,count+1);
    }
    private void incrementDocumentTermFrequency(int documentId, String token){
        if(!this.documentTermFrequency.containsKey(documentId))
            this.documentTermFrequency.put(documentId,new HashMap<>());
        Map<String, Integer> currentDocTermFrequency = this.documentTermFrequency.get(documentId);
        if(!currentDocTermFrequency.containsKey(token))
            currentDocTermFrequency.put(token,0);
        int newCount = currentDocTermFrequency.get(token) + 1;
        currentDocTermFrequency.put(token,newCount);

    }

    @Override
    public void indexDocument(Article article) {
        final int articleId = article.getId();
        final String articleAuthor = article.getAuthor();
        final String articleTitle = article.getTitle();
        final String articleContent = article.getContent();

        List<String> authorPreprocessed = this.preprocessor.getTokens(articleAuthor);
        List<String> titlePreprocessed = this.preprocessor.getTokens(articleTitle);
        List<String> articlePreprocessed = this.preprocessor.getTokens(articleContent);

        processText(titlePreprocessed,articleId,this.titleIndex);
        processText(authorPreprocessed,articleId,this.contentIndex);
        processText(articlePreprocessed,articleId,this.contentIndex);

    }

    @Override
    public boolean isEmpty() {
        return this.titleIndex.isEmpty();
    }

    @Override
    public void convertToVectorModel(VectorModel model) {
        switch (model){
            //by default the index is created as binary - therefore we dont have to do anything extra
            case BINARY -> {
                //convert index into bag of words model (weight of term is determined by its occurrences in doc. collection)
                return;
            }
            case BAG_OF_WORDS -> {
                doBagOfWordsWeighting(titleIndex);
                doBagOfWordsWeighting(contentIndex);

            }
            //convert index into tf-idf model (weight of term is calculated by term frequency, document term frequency and collection size)
            case TF_IDF -> {
                doTfIdfWeighting(titleIndex);
                doTfIdfWeighting(contentIndex);
            }
        }
    }

    //perform tf-idf weighting on terms in index
    private void doTfIdfWeighting(Map<String,List<PostingItem>> index){
        System.out.println("YO");

        Set<String> terms = index.keySet();
        for(String term : terms){
            List<PostingItem> postingList = index.get(term);
            int documentFrequency = postingList.size();
            for(PostingItem item : postingList){
                int documentId = item.getArticleId();
                int termFrequency = this.documentTermFrequency.get(documentId).get(term);
                double weight = this.calculateTfIdf(termFrequency,documentFrequency,getCollectionSize());
                item.setWeight(weight);
            }

        }




        Log.log(Level.INFO,"tf idf vector model created");


    }
    //method creates bag of words vector model - weight of term is given by its frequency
    private void doBagOfWordsWeighting(Map<String,List<PostingItem>> index){
        Set<String> terms = index.keySet();
        for(String term : terms){
            List<PostingItem> postingList = index.get(term);
            final int termFrequency = calculateTermFreq(term);
            for(PostingItem item : postingList)
                item.setWeight(termFrequency);
        }
        Log.log(Level.INFO,"Bag of words model created");



    }

    public double calculateTfIdf(int termFrequency, int documentFrequency, int documentCollectionSize){
        if(termFrequency == 0)return 0.0;
        double tf = 1 + Math.log10(termFrequency);
        double idf = Math.log10(documentCollectionSize/documentFrequency);
        return tf * idf;

    }
    public int calculateTermFreq(String term){
        return this.collectionTermFrequency.get(term);
    }

    public int getCollectionSize(){
        return this.documentTermFrequency.size();
    }


}
