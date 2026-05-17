package com.framework.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Singleton Pattern
 * Reads all settings from config.properties file
 * Loads only once throughout the entire framework
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;

    private static final String CONFIG_PATH =
            "src/test/resources/config/config.properties";

    private ConfigReader() {
        loadProperties();
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    private void loadProperties() {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            properties.load(fis);
            log.info("Config file loaded successfully!");
        } catch (IOException e) {
            log.error("Config file not found at: {}", CONFIG_PATH);
            throw new RuntimeException("Config file not found!", e);
        }
    }

    public String getBrowser() {
        String browser = System.getProperty("browser");
        return (browser != null) ? browser : properties.getProperty("browser", "chrome");
    }

    public String getUrl() {
        return properties.getProperty("url");
    }

    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait", "15"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("page.load.timeout", "30"));
    }

    public boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(
                properties.getProperty("screenshot.on.failure", "true")
        );
    }

    public boolean isHeadless() {
        String headless = System.getProperty("headless");
        return (headless != null)
                ? Boolean.parseBoolean(headless)
                : Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    public String getReportsFolder() {
        return properties.getProperty("reports.folder", "reports/");
    }
}