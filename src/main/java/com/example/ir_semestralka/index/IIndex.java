package com.example.ir_semestralka.index;

import java.util.List;

public interface IIndex {
    boolean addToIndex(String term, List<String> postingList);

}
