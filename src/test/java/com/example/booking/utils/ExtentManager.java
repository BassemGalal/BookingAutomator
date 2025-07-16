package com.example.booking.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manages ExtentReports instance and configuration
 * Provides centralized reporting functionality across the framework
 */
public class ExtentManager {

    private static final Logger logger = LogManager.getLogger(ExtentManager.class);
    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;
    private static String reportPath;

    private static final String REPORTS_DIR = System.getProperty("report.dir", "extent-reports");

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    private static void createInstance() {
        try {
            File reportsDir = new File(REPORTS_DIR);
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            reportPath = REPORTS_DIR + "/ExtentReport_" + timestamp + ".html";

            sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("Booking Automation Test Report");
            sparkReporter.config().setReportName("Booking System Test Results");
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            extent.setSystemInfo("Environment", System.getProperty("environment", "staging"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Browser", System.getProperty("browser", "chrome"));

            logger.info("ExtentReports initialized. Report path: {}", reportPath);

        } catch (Exception e) {
            logger.error("Error initializing ExtentReports: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize ExtentReports", e);
        }
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports flushed. Report available at: {}", reportPath);
        }
    }

    public static void close() {
        if (extent != null) {
            extent.flush();
            extent = null;
            sparkReporter = null;
            logger.info("ExtentReports closed.");
        }
    }

    public static String getReportPath() {
        return reportPath;
    }
}
