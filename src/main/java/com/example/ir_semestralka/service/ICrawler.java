package com.example.ir_semestralka.service;
public interface ICrawler {
    boolean crawlRootPages();
    boolean crawlPage(String url);

}
