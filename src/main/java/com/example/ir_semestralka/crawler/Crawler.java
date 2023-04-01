package com.example.ir_semestralka.crawler;

import com.example.ir_semestralka.utils.IOManager;

import java.util.HashSet;
import java.util.Set;

public class Crawler {

    private Mapper[] mapperWorkers;
    private Parser[] parserWorkers;
    private Set<String> urls;
    private int politenessInterval;

    public Crawler(int parserWorkerPool, int mapperWorkerPool, int politenessInterval){
        this.mapperWorkers = new Mapper[mapperWorkerPool];
        this.parserWorkers = new Parser[parserWorkerPool];
        this.urls = new HashSet<>();
        this.politenessInterval = politenessInterval;
    }

    /**
     * First access point to crawling utility of application
     * crawls BBC articles
     */
    public void crawlSeedPage(boolean rewriteCollection){
        IOManager.createStorage(rewriteCollection);

    }


    public void resetCollection(){
        crawlSeedPage(true);
    }


    /**
     * Second access to crawling utility, crawls only the given url
     * @param url
     */
    public void crawl(String url){

    }

    private void run(){

    }




}
