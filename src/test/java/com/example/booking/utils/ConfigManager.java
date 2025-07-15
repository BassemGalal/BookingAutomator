package com.example.booking.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Loads and manages environment-specific properties
 * Provides centralized configuration management across the framework
 */
public class ConfigManager {
    
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private Properties properties;
    private String currentEnvironment;
    
    private ConfigManager() {
        loadProperties();
    }
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
    
    private void loadProperties() {
        try {
            // Get environment from system property, default to staging
            currentEnvironment = System.getProperty("environment", "staging");
            logger.info("Loading properties for environment: {}", currentEnvironment);
            
            properties = new Properties();
            String propertyFile = String.format("config/%s.properties", currentEnvironment);
            
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertyFile);
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Properties loaded successfully from: {}", propertyFile);
            } else {
                logger.error("Property file not found: {}", propertyFile);
                throw new RuntimeException("Property file not found: " + propertyFile);
            }
            
        } catch (IOException e) {
            logger.error("Error loading properties: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load properties", e);
        }
    }
    
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: {}", key);
        }
        return value;
    }
    
    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        if (value.equals(defaultValue)) {
            logger.warn("Using default value for property: {}", key);
        }
        return value;
    }
    
    public String getCurrentEnvironment() {
        return currentEnvironment;
    }
    
    public void refreshProperties() {
        loadProperties();
    }
}
