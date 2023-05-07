package com.example.ir_semestralka.index;

import com.example.ir_semestralka.global.Constants;
import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.Document;
import com.example.ir_semestralka.model.VectorModel;
import com.example.ir_semestralka.preprocessor.TextPreprocessor;
import com.example.ir_semestralka.utils.Log;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class InvertedIndex implements IIndex{
    private final Map<String,List<PostingItem>> titleIndex;
    private final Map<String,List<PostingItem>> contentIndex;
    private final Map<String,Integer> collectionTermFrequency;
    private final Map<Integer,Map<String,Double>> documentTermWeight;
    private final Map<Integer,Map<String,Integer>> documentTermFrequency;
    private final Map<Integer,Double> documentSize;

    private final TextPreprocessor preprocessor;

    public InvertedIndex(){
        this.titleIndex = new HashMap<>();
        this.contentIndex = new HashMap<>();
        this.preprocessor = new TextPreprocessor();
        this.collectionTermFrequency = new HashMap<>();
        this.documentTermFrequency = new HashMap<>();
        this.documentSize = new HashMap<>();
        this.documentTermWeight =  new HashMap<>();
    }

    public InvertedIndex(Map<String, List<PostingItem>> titleIndex, Map<String, List<PostingItem>> contentIndex, TextPreprocessor preprocessor, Map<String, Integer> collectionTermFrequency, Map<Integer, Map<String, Integer>> documentTermFrequency) {
        this.titleIndex = titleIndex;
        this.contentIndex = contentIndex;
        this.preprocessor = preprocessor;
        this.collectionTermFrequency = collectionTermFrequency;
        this.documentTermFrequency = documentTermFrequency;
        this.documentSize = new HashMap<>();
        this.documentTermWeight =  new HashMap<>();
    }
    //calculate the length of the document
    // the l2 norm of vector
    private double getDocumentLength(int articleId){
        if(documentSize.containsKey(articleId))return documentSize.get(articleId);
        Map<String,Double> tmp = this.documentTermWeight.get(articleId);
        double length = calculateDocumentL2Norm(tmp);
        //store the length of the document - this will be accessed frequently, no need to compute it again
        documentSize.put(articleId,length);
        return length;
    }
    //computes the L2 norm for document from @param documentTerms which contains all terms with weight
    //the norm is just square root of sum of  squares weights
    private double calculateDocumentL2Norm(Map<String, Double> documentTerms){
        double norm = 0;
        Set<String> keys = documentTerms.keySet();
        for(String key : keys){
            double tmp = documentTerms.get(key);
            norm += tmp*tmp;
        }
        return Math.sqrt(norm);
    }

    @Override
    public void saveIndex() {
        File f = new File(Constants.indexRoot);
        if(!f.exists())
            f.mkdir();
        try{
            //save the title index

            f.mkdirs();
            FileOutputStream fs = new FileOutputStream(Constants.titleIndexPath);
            ObjectOutputStream outputStream = new ObjectOutputStream(fs);
            outputStream.writeObject(titleIndex);
            outputStream.close();
            fs.close();

            fs = new FileOutputStream(Constants.contentIndexPath);
            outputStream = new ObjectOutputStream(fs);
            outputStream.writeObject(contentIndex);
            outputStream.close();
            fs.close();

            fs = new FileOutputStream(Constants.collectionTermFrequencyPath);
            outputStream = new ObjectOutputStream(fs);
            outputStream.writeObject(collectionTermFrequency);
            outputStream.close();
            fs.close();

            fs = new FileOutputStream(Constants.documentTermFrequencyPath);
            outputStream = new ObjectOutputStream(fs);
            outputStream.writeObject(documentTermFrequency);
            outputStream.close();
            fs.close();

            fs = new FileOutputStream(Constants.documentSizePath);
            outputStream = new ObjectOutputStream(fs);
            outputStream.writeObject(documentSize);
            outputStream.close();
            fs.close();


        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Log.log(Level.INFO,"Index saved on disk!");
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
        //store the size of document - this information is used in normalization frequently
        //this.documentSize.put(documentId,newCount);
        currentDocTermFrequency.put(token,newCount);

    }

    @Override
    public void indexDocument(Article article) {
        final int articleId = article.getId();
        final String articleTitle = article.getTitle();
        final String articleContent = article.getContent();

        List<String> titlePreprocessed = this.preprocessor.getTokens(articleTitle);
        List<String> articlePreprocessed = this.preprocessor.getTokens(articleContent);

        processText(titlePreprocessed,articleId,this.titleIndex);
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
        Set<String> terms = index.keySet();
        int collectionSize = getCollectionSize();
        for(String term : terms){
            List<PostingItem> postingList = index.get(term);
            int documentFrequency = postingList.size();
            for(PostingItem item : postingList){
                int documentId = item.getArticleId();
                int termFrequency = this.documentTermFrequency.get(documentId).get(term);
                double weight = this.calculateTfIdf(termFrequency,documentFrequency,collectionSize);
                if(!this.documentTermWeight.containsKey(documentId))
                    this.documentTermWeight.put(documentId,new HashMap<>());
                //store term with tf idf weight for every document in hashmap so we dont have to iterate over posting lists
                this.documentTermWeight.get(documentId).put(term,weight);

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

            for(PostingItem item : postingList){
                int documentId = item.getArticleId();
                item.setWeight(termFrequency);
                if(!this.documentTermWeight.containsKey(documentId))
                    this.documentTermWeight.put(documentId,new HashMap<>());
                this.documentTermWeight.get(documentId).put(term,(double)termFrequency);
            }
        }
        Log.log(Level.INFO,"Bag of words model created");



    }

    public double calculateTfIdf(int termFrequency, int documentFrequency, int documentCollectionSize){
        if(termFrequency == 0)return 0.0;
        double tf = 1 + Math.log10(termFrequency);
        double idf = Math.log10((double)documentCollectionSize/documentFrequency);
        return tf * idf;

    }
    public int calculateTermFreq(String term){
        return this.collectionTermFrequency.get(term);
    }

    public int getCollectionSize(){
        return this.documentTermFrequency.size();
    }

    @Override
    public IIndex createDeepCopy() {
        //create a copy of indexes
        Map<String,List<PostingItem>> tmpTitleIndex = copyIndex(this.titleIndex);
        Map<String,List<PostingItem>> tmpContentIndex = copyIndex(this.contentIndex);
        Map<String,Integer> tmpCollectionTermFrequency = new HashMap<>();
        Map<Integer,Map<String,Integer>> tmpDocumentTermFrequency = new HashMap<>();
        Set<String> keys = this.collectionTermFrequency.keySet();
        //copy term frequency - very simple
        for(String key : keys)
            tmpCollectionTermFrequency.put(key,this.collectionTermFrequency.get(key));
        //copy document frequency - a bit more complicated because the structure is nested
        Set<Integer> documentIds = this.documentTermFrequency.keySet();
        for(Integer id : documentIds){
            Map<String,Integer> tmpDocFreq = new HashMap<>();
            tmpDocumentTermFrequency.put(id,tmpDocFreq);
            Map<String,Integer> docFreq = this.documentTermFrequency.get(id);
            keys = docFreq.keySet();
            for(String key : keys){
                tmpDocFreq.put(key,docFreq.get(key));
            }
        }
        return new InvertedIndex(tmpTitleIndex,tmpContentIndex,new TextPreprocessor(),tmpCollectionTermFrequency,tmpDocumentTermFrequency);
    }

    @Override
    public List<Integer> retrieveDocuments(String query,VectorModel vectorModel) {
        //if(vectorModel == VectorModel.BINARY)
        //    return booleanRetrieval(query);
        double queryNorm = 0;
        List<String> tokens = getQueryToken(query);
        //ids of the most relevant articles
        List<Integer> relevantDocuments = new ArrayList<>();
        //all retrieved documents relevant to the query
        Map<Integer,Double> retrieved = new HashMap<>();
        int collectionSize = getCollectionSize();
        //dot product on query and all relevant documents
        for(String token : tokens){
            //unknown word in the collection - ignore it
            if(!this.contentIndex.containsKey(token))continue;
            // number of documents which contain this term
            int documentFrequency = this.contentIndex.get(token).size();
            //each term in the query appears exactly once
            int termFrequency = 1;
            //weight in the query is either 1 - if bag of words model is used or tf-idf value
            double weight = vectorModel==VectorModel.TF_IDF ? calculateTfIdf(termFrequency,documentFrequency,collectionSize) : 1;
            queryNorm += weight*weight;
            //posting list for this term
            List<PostingItem> postingList = this.contentIndex.get(token);

            for(PostingItem item : postingList){
                int articleId = item.getArticleId();
                double itemWeight = item.getWeight();
                double w = (itemWeight * weight);
                if(retrieved.containsKey(articleId)){
                    double oldWeight = retrieved.get(articleId);
                    retrieved.put(articleId,oldWeight+w);
                    continue;
                }

                retrieved.put(articleId,w);
            }




            //3) najit K nejrelevantnejsich
        }
        queryNorm = Math.sqrt(queryNorm);
        Set<Integer> relevantDocumentIds = retrieved.keySet();
        ArrayList<Document> help = new ArrayList<>();
        for(Integer relevantDocumentId : relevantDocumentIds){
            double dotProduct = retrieved.get(relevantDocumentId);
            //cosine distance between query and document
            double relevance = dotProduct / (queryNorm * getDocumentLength(relevantDocumentId));
            help.add(new Document(relevantDocumentId,relevance));
        }
        Object[] docs = help.toArray();
        Arrays.sort(docs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Document d1 = (Document) o1;
                Document d2 = (Document) o2;
                if(d2.getRelevance() > d1.getRelevance())
                    return 1;
                if(d2.getRelevance() < d1.getRelevance())
                    return -1;
                return 0;
            }
        });

        //vytahni prvnich K dokumentu a vrat je - uloz do soubory na FS vsechny ostatni


        return null;
    }
    //boolean retrieval - only AND logical connection
    //TODO IF TIME
    private  List<Integer> booleanRetrieval(String query){
        /*List<String> tokens = getQueryToken(query);
        String first = tokens.get(0);
        //get postings lists of all documents that contain the first word - all the rest is irrelevant
        List<PostingItem> postingLists = this.contentIndex.get(first);

        */
        return null;
    }


    private List<String> getQueryToken(String query){
        List<String> tokens = new ArrayList<>(new HashSet<>(this.preprocessor.getTokens(query)));
        return tokens;
    }

    private Map<String,List<PostingItem>> copyIndex(Map<String,List<PostingItem>> index){
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


}
