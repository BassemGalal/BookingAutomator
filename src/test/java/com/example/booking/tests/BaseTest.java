package com.example.booking.tests;

import com.example.booking.api.ApiClient;
import com.example.booking.ui.DriverManager;
import com.example.booking.utils.ConfigManager;
import com.example.booking.utils.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

/**
 * Parent class for all test classes.
 * Handles setup/teardown for API & UI tests.
 */
public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected static ConfigManager configManager;
    protected static ApiClient apiClient;

    protected DriverManager driverManager;
    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"environment"})
    public void beforeSuite(@Optional("staging") String environment) {
        logger.info("========== Starting Test Suite ==========");

        System.setProperty("environment", environment);
        configManager = ConfigManager.getInstance();
        logger.info("Environment set to: {}", configManager.getProperty("environment"));

        ExtentManager.getInstance();
        logger.info("ExtentReports initialized.");
    }

    @BeforeTest(alwaysRun = true)
    public void beforeTest() {
        logger.info("------ Starting Test ------");
        if (apiClient == null) {
            apiClient = new ApiClient();
            logger.info("API Client initialized.");
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        logger.info(">> Starting test method.");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        logger.info("<< Completed test method.");
        quitDriverIfInitialized();
    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
        logger.info("------ Completed Test ------");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        logger.info("========== Completed Test Suite ==========");
        try {
            ExtentManager.flush();
            logger.info("ExtentReports flushed successfully.");
        } catch (Exception e) {
            logger.error("Failed to flush ExtentReports: {}", e.getMessage(), e);
        }
    }

    // === Driver Utility Methods ===
    protected WebDriver initDriver() {
        if (driverManager == null) {
            driverManager = new DriverManager();
            driverManager.initializeDriver();
            driver = driverManager.getDriver();
            logger.info("WebDriver initialized.");
        }
        return driver;
    }

    protected void quitDriverIfInitialized() {
        if (driverManager != null) {
            driverManager.quitDriver();
            driverManager = null;
            driver = null;
            logger.info("WebDriver quit and cleaned up.");
        }
    }
}
