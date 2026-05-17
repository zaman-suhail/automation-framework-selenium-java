package com.framework.driver;

import com.framework.config.ConfigReader;
import com.framework.factory.BrowserFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

/**
 * DriverManager - Singleton Pattern (Thread-Safe)
 * Only one browser instance exists throughout the framework
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            log.info("Creating new WebDriver...");
            WebDriver driver = BrowserFactory.createDriver();
            setTimeouts(driver);
            driverThread.set(driver);
            log.info("WebDriver is ready!");
        }
        return driverThread.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            log.info("Closing browser...");
            driver.quit();
            driverThread.remove();
            log.info("Browser closed successfully!");
        }
    }

    private static void setTimeouts(WebDriver driver) {
        ConfigReader config = ConfigReader.getInstance();
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(config.getImplicitWait()))
                .pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().window().maximize();
    }
}