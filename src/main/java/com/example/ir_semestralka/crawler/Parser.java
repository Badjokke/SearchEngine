package com.example.ir_semestralka.crawler;

import com.example.ir_semestralka.utils.Global;

import java.util.List;
import java.util.logging.Level;

/**
 * Worker thread for crawling
 * This class asks Crawler for url address and then proceeds to parse it with xPath expressions
 * at the end of run method, workers adds page into Crawler parsedQueue
 */
public class Parser implements Runnable {
    private List<String> xpathExpression;
    private Crawler manager;
    private int politenessInterval;
    public Parser(Crawler manager, List<String> xpathExpressions, int politenessInterval){
        this.manager = manager;
        this.xpathExpression = xpathExpressions;
        this.politenessInterval = politenessInterval;
    }

    @Override
    public void run() {


        try {
            Thread.sleep(politenessInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Global.logger.log(Level.SEVERE,"Parser worker thread sleep exception.");
        }
    }






}
