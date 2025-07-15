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
    
    /**
     * Load test data from JSON file
     */
    public Map<String, Object> loadTestData(String fileName) {
        try {
            logger.info("Loading test data from: {}", fileName);
            
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testdata/" + fileName);
            if (inputStream == null) {
                logger.error("Test data file not found: {}", fileName);
                throw new RuntimeException("Test data file not found: " + fileName);
            }
            
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            Map<String, Object> testData = objectMapper.readValue(inputStream, typeRef);
            
            logger.info("Test data loaded successfully from: {}", fileName);
            return testData;
            
        } catch (IOException e) {
            logger.error("Error loading test data from {}: {}", fileName, e.getMessage(), e);
            throw new RuntimeException("Failed to load test data from " + fileName, e);
        }
    }
    
    /**
     * Load test data array from JSON file
     */
    public List<Map<String, Object>> loadTestDataArray(String fileName) {
        try {
            logger.info("Loading test data array from: {}", fileName);
            
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testdata/" + fileName);
            if (inputStream == null) {
                logger.error("Test data file not found: {}", fileName);
                throw new RuntimeException("Test data file not found: " + fileName);
            }
            
            TypeReference<List<Map<String, Object>>> typeRef = new TypeReference<List<Map<String, Object>>>() {};
            List<Map<String, Object>> testData = objectMapper.readValue(inputStream, typeRef);
            
            logger.info("Test data array loaded successfully from: {}", fileName);
            return testData;
            
        } catch (IOException e) {
            logger.error("Error loading test data array from {}: {}", fileName, e.getMessage(), e);
            throw new RuntimeException("Failed to load test data array from " + fileName, e);
        }
    }
    
    /**
     * Convert object to JSON string
     */
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Error converting object to JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
    
    /**
     * Convert JSON string to object
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("Error converting JSON to object: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }
}
