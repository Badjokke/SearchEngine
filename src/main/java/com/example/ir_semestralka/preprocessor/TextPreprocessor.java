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

    public String preprocessWord(String word){
        word = word.toLowerCase(Locale.ROOT);
        word = stripParenthesisAndNumbers(word);
        word = stripDates(word);
        word = stripUrls(word);
        word = stripHtml(word);
        return this.stemmer.stem(word);
    }
    public List<String> getTokens(String text){
        return this.preprocessText(text);
    }
    private List<String> preprocessText(String text){
        if(text == null)return null;

        //toss out all brackets, all quotation marks and numbers - zero value for us
        text = stripParenthesisAndNumbers(text);

        //toss out all the dates
        text = stripDates(text);
        //get rid of all urls
        text = stripUrls(text);
        //get rid of emails
        text = stripEmails(text);
        //split into token by common delimiters
        String[] words = text.split("\\s+|\\-|\\:|\\.|\\,|\\?|\\!");
        List<String> preprocessedText = new ArrayList<>();
        for(int i = 0; i < words.length; i++){
            if(words[i].length() < 2)continue;
            //convert to lowercase
            String word = words[i].toLowerCase(Locale.ROOT);
            //toss out html tags
            word = stripHtml(word);
            if(this.vocabulary.contains(word))
                continue;
            //stem the word
            preprocessedText.add(this.stemmer.stem(word));
        }
        return preprocessedText;
    }
    private String stripParenthesisAndNumbers(String text){
        text = text
                .replaceAll("\\(|\\)|\\[|\\]|\\{|\\}|\"|\\d+|\\@|\\/","");
        return text;
    }

    private String stripHtml(String word){
        if(word == null)return null;
        if(word.charAt(0) != '<')return word;
        //remove html tags
        //html is not a natural language, this regex will fail in some obscure cases
        //but it is good enough for bbc articles
        word = word.replaceAll("<\\/?!?(\\w+)[^>]*>","");
        return word;
    }
    //regex for dates in format
    //dd/mm/yyyy, dd-mm-yyyy or dd.mm.yyyy
    private String stripDates(String text){
        //regex for date format
        final String pattern = "([0-9]{1,4}(\\.|\\-|\\/)[0-9]{1,2}(\\.|\\-|\\/)[0-9]{1,4})";
        return text.replaceAll(pattern,"");
    }
    //strip down all urls in text
    private String stripUrls(String text){
        final String pattern = "((https:\\/\\/|http:\\/\\/)(www)?[A-Za-z]+\\.[A-Za-z]+[\\/A-Za-z\\-\\.\\?]*)";
        return text.replaceAll(pattern,"");
    }
    private String stripEmails(String text){
        final String pattern = "([A-Za-z0-9\\.\\-\\_]+\\@[A-Za-z0-9\\.\\-\\_]+\\.\\w+)";
        return text.replaceAll(pattern,"");
    }




}
