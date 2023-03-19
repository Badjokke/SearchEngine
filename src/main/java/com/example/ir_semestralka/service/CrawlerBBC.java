package com.example.ir_semestralka.service;

import com.example.ir_semestralka.utils.Global;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Level;

@Service
public class CrawlerBBC implements ICrawler{
    @Override
    public JSONObject downloadPage(String url, Map<String, String> xpaths) {
        Global.logger.log(Level.INFO,"downloading page "+ url);
        return null;
    }
}
