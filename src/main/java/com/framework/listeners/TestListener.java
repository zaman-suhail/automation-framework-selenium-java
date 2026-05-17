package com.framework.listeners;

import com.aventstack.extentreports.Status;
import com.framework.driver.DriverManager;
import com.framework.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener - Observer Pattern
 * Listens to TestNG events and automatically:
 * - Logs pass/fail/skip in the HTML report
 * - Takes screenshot on failure
 * - Saves everything to log file
 */
public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("========================================");
        log.info("Test Suite started: {}", context.getName());
        log.info("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        log.info("------ Test started: {} ------", testName);

        // Create test entry in HTML report
        ExtentReportManager.createTest(
                testName,
                description.isEmpty() ? testName : description
        );
        ExtentReportManager.logInfo("Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.info("✅ PASSED: {}", testName);
        ExtentReportManager.logPass("Test PASSED: " + testName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMsg = result.getThrowable().getMessage();

        log.error("❌ FAILED: {} | Error: {}", testName, errorMsg);
        ExtentReportManager.logFail("Test FAILED: " + testName);
        ExtentReportManager.logFail("Error: " + errorMsg);

        // Take screenshot and attach to report
        try {
            WebDriver driver = DriverManager.getDriver();
            String base64Screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BASE64);
            ExtentReportManager.getTest()
                    .addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
            log.info("Screenshot attached to report");
        } catch (Exception e) {
            log.warn("Could not take screenshot: {}", e.getMessage());
        }

        // Attach full stack trace to report
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.warn("⏭️ SKIPPED: {}", testName);
        ExtentReportManager.logWarning("Test SKIPPED: " + testName);
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("========================================");
        log.info("Suite finished | Pass: {} | Fail: {} | Skip: {}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        log.info("========================================");

        // Save HTML report to disk
        ExtentReportManager.flushReports();
    }
}