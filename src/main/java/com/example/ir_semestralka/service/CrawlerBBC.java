package com.example.ir_semestralka.service;


import com.example.ir_semestralka.crawler.Crawler;
import org.springframework.stereotype.Service;

@Service
public class CrawlerBBC implements ICrawler{
    @Override
    public boolean crawlRootPages() {
        Crawler crawler = new Crawler(10);
        //start crawler thread so the server can still serve clients
        crawler.config();
        crawler.crawlSeedPage();

        return true;
    }

    @Override
    public boolean crawlPage(String url) {
        return true;
    }
}
