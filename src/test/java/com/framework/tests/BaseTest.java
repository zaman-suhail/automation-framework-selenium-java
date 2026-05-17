package com.framework.tests;

import com.framework.config.ConfigReader;
import com.framework.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest - Template Method Pattern
 * Parent class for all test classes
 * Handles setup and teardown so every test does not repeat it
 */
public abstract class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected ConfigReader config;

    @BeforeMethod
    public void setUp() {
        log.info("===== Test started =====");
        config = ConfigReader.getInstance();
        driver = DriverManager.getDriver();
        driver.get(config.getUrl());
        log.info("Website opened: {}", config.getUrl());
    }

    @AfterMethod
    public void tearDown() {
        log.info("===== Test finished =====");
        DriverManager.quitDriver();
    }
}