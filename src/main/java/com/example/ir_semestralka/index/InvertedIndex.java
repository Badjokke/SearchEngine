package com.example.ir_semestralka.index;

import java.util.List;

public class InvertedIndex implements IIndex{

    public InvertedIndex(){

    }



    @Override
    public boolean addToIndex(String term, List<String> postingList) {
        return false;
    }

}
