package com.example.ir_semestralka.index;

import com.example.ir_semestralka.global.Constants;
import com.example.ir_semestralka.model.Article;
import com.example.ir_semestralka.model.Document;
import com.example.ir_semestralka.model.VectorModel;
import com.example.ir_semestralka.preprocessor.TextPreprocessor;
import com.example.ir_semestralka.utils.InvertedIndexUtils;
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
    private final InvertedIndexUtils utils;
    private final TextPreprocessor preprocessor;
    //threshold from which documents are no longer considered relevant
    private final double THRESH_HOLD = 0.15;
    private final int PAGE_SIZE = 20;
    public InvertedIndex(){
        this.titleIndex = new HashMap<>();
        this.contentIndex = new HashMap<>();
        this.preprocessor = new TextPreprocessor();
        this.collectionTermFrequency = new HashMap<>();
        this.documentTermFrequency = new HashMap<>();
        this.documentSize = new HashMap<>();
        this.documentTermWeight =  new HashMap<>();
        utils = new InvertedIndexUtils();
    }

    public InvertedIndex(Map<String, List<PostingItem>> titleIndex, Map<String, List<PostingItem>> contentIndex, TextPreprocessor preprocessor, Map<String, Integer> collectionTermFrequency, Map<Integer, Map<String, Integer>> documentTermFrequency) {
        this.titleIndex = titleIndex;
        this.contentIndex = contentIndex;
        this.preprocessor = preprocessor;
        this.collectionTermFrequency = collectionTermFrequency;
        this.documentTermFrequency = documentTermFrequency;
        this.documentSize = new HashMap<>();
        this.documentTermWeight =  new HashMap<>();
        utils = new InvertedIndexUtils();

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
    public void saveIndex(VectorModel model) {
        utils.saveIndex(titleIndex,contentIndex,collectionTermFrequency,documentTermFrequency,documentSize,model);
        Log.log(Level.INFO,"Index saved on disk!");
    }

    @Override
    public void loadIndex(VectorModel model) {
        utils.loadIndex(titleIndex,contentIndex,collectionTermFrequency,documentTermFrequency,documentSize,model);

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
    //adds document to inverted index with binary weights
    //this method is used when the index is being created
    //the weighting is performed after construction of the base data structure
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

    //assign weights to items in posting list
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
            tfIdfWeighting(postingList,term,collectionSize);
        }
        Log.log(Level.INFO,"tf idf vector model created");


    }
    //method creates bag of words vector model - weight of term is given by its frequency in collection
    private void doBagOfWordsWeighting(Map<String,List<PostingItem>> index){
        Set<String> terms = index.keySet();
        for(String term : terms){
            List<PostingItem> postingList = index.get(term);
            bagOfWordsWeight(postingList,term);
        }
        Log.log(Level.INFO,"Bag of words model created");
    }

    private void bagOfWordsWeight(List<PostingItem> postingList, String term){
        final int termFrequency = calculateTermFreq(term);
        for(PostingItem item : postingList){
            int documentId = item.getArticleId();
            item.setWeight(termFrequency);
            if(!this.documentTermWeight.containsKey(documentId))
                this.documentTermWeight.put(documentId,new HashMap<>());
            this.documentTermWeight.get(documentId).put(term,(double)termFrequency);
        }
    }

    private void tfIdfWeighting(List<PostingItem> postingList, String term, int collectionSize){
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


    //compute tf-idf weight for given term
    //tf idf weight value is dependent on the documentFrequency - how many documents contain the term
    //termFrequency - how often does the term appear in the entire collection = rarer terms have higher value
    //and lastly the number of all documents in our collection
    public double calculateTfIdf(int termFrequency, int documentFrequency, int documentCollectionSize){
        return utils.calculateTfIdf(termFrequency,documentFrequency,documentCollectionSize);
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
        return this.utils.createDeepCopy(titleIndex,contentIndex,collectionTermFrequency,documentTermFrequency);
    }
    private Object[] retrieveDocumentsFromIndex(List<String>queryTerms, VectorModel model, Map<String,List<PostingItem>> index){
        double queryNorm = 0;
        Map<Integer,Double> retrieved = new HashMap<>();
        int collectionSize = getCollectionSize();
        //dot product on query and all relevant documents
        for(String token : queryTerms){
            //unknown word in the collection - ignore it
            if(!index.containsKey(token))continue;
            // number of documents which contain this term
            int documentFrequency = index.get(token).size();
            //each term in the query appears exactly once
            int termFrequency = 1;
            //weight in the query is either 1 - if bag of words model is used or tf-idf value
            double weight = model==VectorModel.TF_IDF ? calculateTfIdf(termFrequency,documentFrequency,collectionSize) : 1;
            queryNorm += weight*weight;
            //posting list for this term
            List<PostingItem> postingList = index.get(token);

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
        }
        queryNorm = Math.sqrt(queryNorm);
        Set<Integer> relevantDocumentIds = retrieved.keySet();
        ArrayList<Document> help = new ArrayList<>();
        for(Integer relevantDocumentId : relevantDocumentIds){
            double dotProduct = retrieved.get(relevantDocumentId);
            //cosine distance between query and document
            double relevance = dotProduct / (queryNorm * getDocumentLength(relevantDocumentId));
            if(relevance > THRESH_HOLD)
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
        return docs;
    }
    @Override
    public List<Integer> retrieveDocuments(String query,VectorModel vectorModel) {
        //if(vectorModel == VectorModel.BINARY)
        //    return booleanRetrieval(query);
        List<Integer> articleIds = new ArrayList<>();
        List<String> tokens = getQueryToken(query);
        Object[] relevantDocumentsByTitle = retrieveDocumentsFromIndex(tokens,vectorModel,titleIndex);
        Object[] relevantDocumentsByContent = null;
        if(relevantDocumentsByTitle.length < PAGE_SIZE){
            relevantDocumentsByContent = retrieveDocumentsFromIndex(tokens,vectorModel,contentIndex);
        }
        //merge two sorted arrays into list of documents
        if(relevantDocumentsByContent != null){
            return mergeDocumentArrays(relevantDocumentsByTitle,relevantDocumentsByContent);
        }

        for(int i = 0; i <relevantDocumentsByTitle.length; i++){
            Document d = (Document) relevantDocumentsByTitle[i];
            articleIds.add(d.getId());
        }
        return articleIds;
    }

    private List<Integer> mergeDocumentArrays(Object[] relevantDocumentsByTitle, Object[] relevantDocumentsByContent) {
        Set<Integer> ids = new HashSet<>();
        int titlePointer = 0;
        int contentPointer = 0;
        while(true){
            if(titlePointer == relevantDocumentsByTitle.length && contentPointer == relevantDocumentsByContent.length)
                break;
            //copy rest of the content documents
            if(titlePointer == relevantDocumentsByTitle.length){
                for(int i = contentPointer; i < relevantDocumentsByContent.length;i++){
                    Document d = (Document) relevantDocumentsByContent[i];
                    ids.add(d.getId());
                }
                break;
            }
            //copy rest of the title documents
            if(contentPointer == relevantDocumentsByContent.length){
                for(int i = titlePointer; i < relevantDocumentsByTitle.length;i++){
                    Document d = (Document) relevantDocumentsByTitle[i];
                    ids.add(d.getId());
                }
                break;
            }
            Document titleDocument = (Document)relevantDocumentsByTitle[titlePointer];
            Document contentDocument = (Document) relevantDocumentsByContent[contentPointer];

            if(titleDocument.getRelevance()>contentDocument.getRelevance()){
                ids.add(titleDocument.getId());
                titlePointer++;
                continue;
            }
            ids.add(contentDocument.getId());
            contentPointer++;


        }
        return ids.stream().toList();
    }

    @Override
    public void indexNewDocument(Article article,VectorModel vectorModel) {
        final int articleId = article.getId();
        final String articleTitle = article.getTitle();
        final String articleContent = article.getContent();

        List<String> titlePreprocessed = this.preprocessor.getTokens(articleTitle);
        List<String> articlePreprocessed = this.preprocessor.getTokens(articleContent);

        processText(titlePreprocessed,articleId,this.titleIndex);
        processText(articlePreprocessed,articleId,this.contentIndex);
        //recalculate weights
        //should be done for the entire index, because they term frequency / document frequency is changed,
        //but it is too expensive
        recalculatePostingListsWeight(titlePreprocessed,titleIndex, vectorModel);
        recalculatePostingListsWeight(articlePreprocessed,contentIndex,vectorModel);

    }
    //recalculate the posting list weights for new document
    private void recalculatePostingListsWeight(List<String> terms, Map<String,List<PostingItem>> index, VectorModel model){
        //by default the document is indexed with binary weights
        if(model == VectorModel.BINARY)return;
        int collectionSize = getCollectionSize();
        for(String term : terms){
            List<PostingItem> l = index.get(term);
            switch (model){
                case TF_IDF -> tfIdfWeighting(l,term,collectionSize);
                case BAG_OF_WORDS -> bagOfWordsWeight(l,term);
            }

        }
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

    //preprocess query and remove duplicate terms
    private List<String> getQueryToken(String query){
        return new ArrayList<>(new HashSet<>(this.preprocessor.getTokens(query)));
    }

}
