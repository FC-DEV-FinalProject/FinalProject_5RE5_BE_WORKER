package com.oreo.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getInstance() {
        return objectMapper;
    }
}
