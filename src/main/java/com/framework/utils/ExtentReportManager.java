package com.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportManager - Singleton Pattern
 * Creates an HTML report after every test run
 * Shows pass, fail, skip results with screenshots
 */
public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    private ExtentReportManager() {}

    // Create report instance - only once
    public static ExtentReports getInstance() {
        if (extentReports == null) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = "reports/TestReport_" + timestamp + ".html";

            // Create reports folder if it does not exist
            new java.io.File("reports").mkdirs();

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Saucedemo Test Results");
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java", System.getProperty("java.version"));
            extentReports.setSystemInfo("Browser", "Chrome");
            extentReports.setSystemInfo("URL", "https://www.saucedemo.com");

            log.info("Extent Report created: {}", reportPath);
        }
        return extentReports;
    }

    // Create a new test entry in report
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        testThread.set(test);
        log.info("Test started in report: {}", testName);
        return test;
    }

    // Get current test
    public static ExtentTest getTest() {
        return testThread.get();
    }

    // Clean up after test
    public static void removeTest() {
        testThread.remove();
    }

    // Save report to disk
    public static void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            log.info("Report saved successfully!");
        }
    }

    // --- Logging helpers ---

    public static void logInfo(String message) {
        if (getTest() != null) getTest().info(message);
        log.info(message);
    }

    public static void logPass(String message) {
        if (getTest() != null) getTest().pass(message);
        log.info("PASS: {}", message);
    }

    public static void logFail(String message) {
        if (getTest() != null) getTest().fail(message);
        log.error("FAIL: {}", message);
    }

    public static void logWarning(String message) {
        if (getTest() != null) getTest().warning(message);
        log.warn("WARN: {}", message);
    }
}