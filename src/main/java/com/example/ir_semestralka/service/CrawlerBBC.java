package com.example.ir_semestralka.service;


import com.example.ir_semestralka.crawler.Crawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrawlerBBC implements ICrawler{
    @Autowired
    SearchEngine searchEngineService;
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
        Crawler crawler = new Crawler(1);
        crawler.config();
        if(!url.contains(crawler.getRootPage()))
            return false;
        boolean crawled = crawler.crawl(url);
        if(!crawled)
            return false;
        //get the id of created article
        int lastArticleId = crawler.getCurrentArticleId()-1;
        //index the downloaded article
        searchEngineService.indexArticle(lastArticleId);
        return true;
    }
}
