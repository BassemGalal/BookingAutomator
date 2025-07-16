package com.example.booking.ui.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

/**
 * Base class for all page objects
 * Contains common methods and WebDriver setup
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    private static final long DEFAULT_WAIT_SECONDS = 20;

    public BasePage(WebDriver driver) {
        this(driver, DEFAULT_WAIT_SECONDS);
    }

    public BasePage(WebDriver driver, long timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigate to a specific URL
     */
    public void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
        waitForPageLoad();
    }

    /**
     * Get current page title
     */
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Current page title: {}", title);
        return title;
    }

    /**
     * Get current page URL
     */
    public String getCurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        logger.info("Current page URL: {}", currentUrl);
        return currentUrl;
    }

    /**
     * Wait until the page is fully loaded
     */
    public void waitForPageLoad() {
        try {
            wait.until((ExpectedCondition<Boolean>) wd ->
                    ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
            logger.info("Page loaded successfully.");
        } catch (Exception e) {
            logger.warn("Timeout or error while waiting for page load: {}", e.getMessage());
        }
    }
}
