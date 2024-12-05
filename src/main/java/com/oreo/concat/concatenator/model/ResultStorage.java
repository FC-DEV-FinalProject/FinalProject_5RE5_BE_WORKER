package com.oreo.concat.concatenator.model;

import java.util.ArrayList;
import java.util.List;

public class ResultStorage<T> {
    private final List<T> resultStorage = new ArrayList<>();

    public void add(T result) {
       resultStorage.add(result);
    }
}
