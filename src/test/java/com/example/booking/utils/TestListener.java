package com.example.booking.utils;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ExtentReports;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for test execution reporting and monitoring
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting Test Suite: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finishing Test Suite: {}", context.getName());
        ExtentManager.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting Test: {}", result.getName());
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test Passed: {}", result.getName());
        extentTest.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test Failed: {}", result.getName(), result.getThrowable());
        extentTest.get().log(Status.FAIL, "Test Failed: " + result.getThrowable());

        // Placeholder for screenshot capture
        // String screenshotPath = ScreenshotUtil.captureScreenshot(result.getName());
        // extentTest.get().addScreenCaptureFromPath(screenshotPath);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test Skipped: {}", result.getName());
        extentTest.get().log(Status.SKIP, "Test Skipped");
    }
}
