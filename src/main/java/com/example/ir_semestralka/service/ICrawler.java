package com.example.ir_semestralka.service;

import org.json.JSONObject;

import java.util.Map;

public interface ICrawler {
    JSONObject downloadPage(String url, Map<String, String> xpaths);

}
