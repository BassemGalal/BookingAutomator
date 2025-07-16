package com.example.booking.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.List;

/**
 * Utility class for reading and parsing JSON test data
 * Handles JSON file operations and data conversion
 */
public class JsonUtil {

    private static final Logger logger = LogManager.getLogger(JsonUtil.class);
    private final ObjectMapper objectMapper;

    public JsonUtil() {
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Object> loadTestData(String fileName) {
        try (InputStream inputStream = getFileAsStream(fileName)) {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
            return objectMapper.readValue(inputStream, typeRef);
        } catch (IOException e) {
            logger.error("Error loading test data from {}: {}", fileName, e.getMessage(), e);
            throw new RuntimeException("Failed to load test data from " + fileName, e);
        }
    }

    public List<Map<String, Object>> loadTestDataArray(String fileName) {
        try (InputStream inputStream = getFileAsStream(fileName)) {
            TypeReference<List<Map<String, Object>>> typeRef = new TypeReference<>() {};
            return objectMapper.readValue(inputStream, typeRef);
        } catch (IOException e) {
            logger.error("Error loading test data array from {}: {}", fileName, e.getMessage(), e);
            throw new RuntimeException("Failed to load test data array from " + fileName, e);
        }
    }

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Error converting object to JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("Error converting JSON to object: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }

    private InputStream getFileAsStream(String fileName) {
        String path = "testdata/" + fileName;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            logger.error("Test data file not found: {}", path);
            throw new RuntimeException("Test data file not found: " + path);
        }
        logger.info("Loading test data from: {}", path);
        return inputStream;
    }
}
