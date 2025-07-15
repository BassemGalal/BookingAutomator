package com.example.booking.tests;

import com.example.booking.api.ApiClient;
import com.example.booking.utils.ConfigManager;
import com.example.booking.utils.ExtentManager;
import org.testng.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Parent class for all test classes
 * Contains common setup, teardown, and utility methods
 */
public class BaseTest {
    
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected ApiClient apiClient;
    protected ConfigManager configManager;
    
    @BeforeSuite
    public void beforeSuite() {
        logger.info("Starting test suite execution");
        
        // Initialize configuration manager
        configManager = ConfigManager.getInstance();
        logger.info("Environment: {}", configManager.getProperty("environment"));
        
        // Initialize ExtentReports
        ExtentManager.getInstance();
        logger.info("ExtentReports initialized");
    }
    
    @BeforeTest
    public void beforeTest() {
        logger.info("Starting test execution");
        
        // Initialize API client
        apiClient = new ApiClient();
        logger.info("API client initialized");
    }
    
    @BeforeMethod
    public void beforeMethod() {
        logger.info("Starting test method execution");
    }
    
    @AfterMethod
    public void afterMethod() {
        logger.info("Completed test method execution");
    }
    
    @AfterTest
    public void afterTest() {
        logger.info("Completed test execution");
    }
    
    @AfterSuite
    public void afterSuite() {
        logger.info("Completed test suite execution");
        
        // Flush ExtentReports
        ExtentManager.flush();
        logger.info("ExtentReports flushed");
    }
    
}
