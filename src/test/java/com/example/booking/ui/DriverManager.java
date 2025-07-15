package com.example.booking.ui;

import com.example.booking.utils.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

/**
 * Manages WebDriver instances across test execution
 * Handles browser initialization, configuration, and cleanup
 */
public class DriverManager {
    
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private final ConfigManager configManager;
    
    public DriverManager() {
        this.configManager = ConfigManager.getInstance();
    }
    
    /**
     * Initialize WebDriver based on configuration
     */
    public void initializeDriver() {
        try {
            String browserName = configManager.getProperty("browser", "chrome");
            boolean headless = Boolean.parseBoolean(configManager.getProperty("browser.headless", "false"));
            
            logger.info("Initializing {} driver, headless: {}", browserName, headless);
            
            WebDriver driver;
            
            switch (browserName.toLowerCase()) {
                case "chrome":
                    driver = createChromeDriver(headless);
                    break;
                case "firefox":
                    driver = createFirefoxDriver(headless);
                    break;
                default:
                    logger.warn("Unknown browser: {}, defaulting to Chrome", browserName);
                    driver = createChromeDriver(headless);
                    break;
            }
            
            // Configure driver
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Long.parseLong(configManager.getProperty("implicit.wait", "10"))
            ));
            
            if (Boolean.parseBoolean(configManager.getProperty("browser.window.maximize", "true"))) {
                driver.manage().window().maximize();
            }
            
            driverThreadLocal.set(driver);
            logger.info("Driver initialized successfully");
            
        } catch (Exception e) {
            logger.error("Error initializing driver: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize driver", e);
        }
    }
    
    /**
     * Create Chrome driver with options
     */
    private WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        
        return new ChromeDriver(options);
    }
    
    /**
     * Create Firefox driver with options
     */
    private WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Get current thread's WebDriver instance
     */
    public WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            logger.error("Driver not initialized for current thread");
            throw new RuntimeException("Driver not initialized. Call initializeDriver() first.");
        }
        return driver;
    }
    
    /**
     * Quit driver and clean up
     */
    public void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting driver: {}", e.getMessage(), e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
    
    /**
     * Close all browser windows
     */
    public void closeDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.close();
                logger.info("Driver closed successfully");
            } catch (Exception e) {
                logger.error("Error closing driver: {}", e.getMessage(), e);
            }
        }
    }
}
