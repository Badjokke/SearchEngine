package com.example.ir_semestralka.preprocessor;

import com.example.ir_semestralka.global.Constants;
import com.example.ir_semestralka.utils.IOManager;
import opennlp.tools.stemmer.PorterStemmer;

import java.util.*;

/**
 * class responsible for preprocessing natural language
 * ie removing stop words, removing duplicities and stemming
 * the class uses PortStammer for stamming
 */
public class TextPreprocessor {
    //Port´s stemming algorithm
    private final PorterStemmer stemmer;
    private Set<String> vocabulary;

    public TextPreprocessor(){
        this.stemmer = new PorterStemmer();
        this.loadVocabulary();
    }


    private void loadVocabulary(){
        String[] vocabulary = IOManager.loadFileContent(Constants.STOP_WORDS_FILE_PATH,"\n");

        if(vocabulary == null){
            this.vocabulary = new HashSet<>();
            return;
        }
        this.vocabulary = new HashSet<>(Arrays.asList(vocabulary));
    }


    public List<String> getTokens(String text){
        return this.preprocessText(text);
    }

    private List<String> preprocessText(String text){
        if(text == null)return null;
        String[] words = text.split("\\s+");
        List<String> preprocessedText = new ArrayList<>();
        for(int i = 0; i < words.length; i++){
            String word = words[i];
            word = word.toLowerCase(Locale.ROOT).replaceAll("(^[\\d]+$)","");
            if(this.vocabulary.contains(word))
                continue;
            preprocessedText.add(this.stemmer.stem(word));
        }
        return preprocessedText;
    }






}
