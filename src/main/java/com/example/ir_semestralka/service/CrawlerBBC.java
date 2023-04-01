package com.example.ir_semestralka.service;


import com.example.ir_semestralka.Constants;
import com.example.ir_semestralka.crawler.Crawler;
import org.springframework.stereotype.Service;

@Service
public class CrawlerBBC implements ICrawler{
    @Override
    public boolean crawlRootPages() {
        Crawler crawler = new Crawler(4,4, Constants.POLITENESS_INTERVAL);
        crawler.crawlSeedPage(false);
        return true;
    }

    @Override
    public boolean crawlPage(String url) {
        return true;
    }
}
