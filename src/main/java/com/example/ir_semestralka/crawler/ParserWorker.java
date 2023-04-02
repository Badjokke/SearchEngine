package com.example.ir_semestralka.crawler;

import com.example.ir_semestralka.Constants;
import com.example.ir_semestralka.utils.IOManager;
import com.example.ir_semestralka.utils.Log;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.crawler.exceptions.ParseException;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.NotAllowedContentException;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.parser.Parser;
import org.apache.http.HttpStatus;
import org.apache.tomcat.util.bcel.Const;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Worker thread for crawling
 * This class asks Crawler for url address and then proceeds to parse it with xPath expressions
 * at the end of run method, workers adds page into Crawler parsedQueue
 */
public class ParserWorker extends Thread {
    private List<String> xpathExpressions;
    private Crawler manager;
    private int politenessInterval;
    private final PageFetcher pageFetcher;
    private final Parser parser;

    public ParserWorker(Crawler manager, List<String> xpathExpressions, int politenessInterval){
        this.manager = manager;
        this.xpathExpressions = xpathExpressions;
        this.politenessInterval = politenessInterval;

        //default crawler lib config
        CrawlConfig config = new CrawlConfig();
        this.pageFetcher = new PageFetcher(config);
        this.parser = new Parser(config);

        config.setMaxDepthOfCrawling(0);
        config.setResumableCrawling(false);
    }

    @Override
    public void run() {
        while(true) {
            String url = this.manager.getUrl();
            //no url is available for parsing, end the thread
            if(url == null)break;
            Page p = parse(url);
            //some faulty url, continue
            if(p == null)continue;
            List<List<String>> parsedData = evalPage(p);
            processArticle(parsedData);

            try {
                Thread.sleep(politenessInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.log(Level.SEVERE, "Parser worker thread sleep exception.");
            }
        }
    }
    //TODO prohnat stazena data preprocessorem textu
    private void processArticle(List<List<String>> parsedData) {
            List<String> titles = parsedData.get(0);
            List<String> authors = parsedData.get(1);
            List<String> content = parsedData.get(2);
            List<String> relatedArticles = parsedData.get(3);
            for(String url : relatedArticles)
                this.manager.addUrlToNestedQueue(url);
            createBBCJSONFile(titles,authors,content);



    }
    //todo move to utils
    private String buildStringFromList(List<String> l){
        String result = "";
        StringBuilder sb = new StringBuilder();
        for(String t : l){
            sb.append(t.replace("\n",""));
        }
        result = sb.toString();
        return result;
    }


    private void createBBCJSONFile(List<String> titles,List<String> authors,List<String> content){
        JSONObject json = new JSONObject();
        String title = buildStringFromList(titles);
        String author = buildStringFromList(authors);
        String text = buildStringFromList(content);

        json.put("title",title);
        json.put("author",author);
        json.put("news_category","general");
        json.put("content",text);


        String jsonString = json.toJSONString();
        try{
            IOManager.writeJSONfile(jsonString, Constants.crawlerFileStorage+"/article"+manager.getArticleNumber()+".json");
        }
        catch (IOException exception){
            exception.printStackTrace();
        }

    }

    private Page parse(String url){
        Log.log(Level.INFO,"Parsing url: "+url);
        WebURL curURL = new WebURL();
        curURL.setURL(url);
        Page page = null;
        PageFetchResult fetchResult = null;
        try{
            fetchResult = pageFetcher.fetchPage(curURL);
            if (fetchResult.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY) {
                curURL.setURL(fetchResult.getMovedToUrl());
                fetchResult = pageFetcher.fetchPage(curURL);
            }
            if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {

               page = new Page(curURL);
               fetchResult.fetchContent(page);
               parser.parse(page, curURL.getURL());
               return page;

            }

        } catch (PageBiggerThanMaxSizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (NotAllowedContentException | ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    private List<List<String>> evalPage(Page page){
        ParseData parseData = page.getParseData();
        List<List<String>> data = new ArrayList<>();
        if (parseData != null) {
            if (parseData instanceof HtmlParseData) {

                Document document = Jsoup.parse(((HtmlParseData) parseData).getHtml());
                for(String xpathExpression : this.xpathExpressions){
                    List<String> xlist = Xsoup.compile(xpathExpression).evaluate(document).list();
                    data.add(xlist);
                }
            }
        }
        return data;
    }


    public List<List<String>> crawlUrls(String url){
        List<List<String>> urls = null;
        Page page = parse(url);
        //failed to parse resource at @param url
        if(page == null) return urls;
        urls = evalPage(page);
        return  urls;
    }


    public void setXPaths(List<String> xPaths){
        this.xpathExpressions = xPaths;
    }



}
