package com.example.ir_semestralka.search_engine;

import com.example.ir_semestralka.model.UserQuery;

public class DefaultQueryParser implements QueryParser{


    @Override
    public String[] parseQuery(String query) {
        String[] terms = query.split("\\s+");
        return terms;
    }
}
