package com.oreo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class RequestSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    public static Map<String, Object> convertToMap(ObjectMapper objectMapper, String message)
        throws JsonProcessingException {
        return objectMapper.readValue(message, Map.class);
    }
}
