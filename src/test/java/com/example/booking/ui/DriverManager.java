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

public class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private final ConfigManager configManager = ConfigManager.getInstance();

    public void initializeDriver() {
        String browserName = configManager.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(configManager.getProperty("browser.headless", "false"));

        logger.info("Initializing browser: {}, headless: {}", browserName, headless);
        WebDriver driver;

        switch (browserName) {
            case "chrome":
                driver = createChromeDriver(headless);
                break;
            case "firefox":
                driver = createFirefoxDriver(headless);
                break;
            default:
                logger.warn("Unsupported browser '{}', defaulting to Chrome.", browserName);
                driver = createChromeDriver(headless);
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Long.parseLong(configManager.getProperty("implicit.wait", "10"))
        ));

        if (!headless && Boolean.parseBoolean(configManager.getProperty("browser.window.maximize", "true"))) {
            driver.manage().window().maximize();
        }

        driverThreadLocal.set(driver);
        logger.info("Driver initialized successfully for thread: {}", Thread.currentThread().getId());
    }

    private WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080"); // avoid rendering issues in headless
        }

        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--disable-extensions",
                "--disable-popup-blocking",
                "--disable-notifications",
                "--disable-web-security",
                "--allow-running-insecure-content"
        );

        return new ChromeDriver(options);
    }

    private WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        return new FirefoxDriver(options);
    }

    public WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Call initializeDriver() first.");
        }
        return driver;
    }

    public void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit successfully for thread: {}", Thread.currentThread().getId());
            } catch (Exception e) {
                logger.error("Error quitting driver: {}", e.getMessage(), e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
