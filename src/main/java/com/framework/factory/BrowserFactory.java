package com.framework.factory;

import com.framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * BrowserFactory - Factory Pattern
 * Reads browser name from config and creates the correct WebDriver
 */
public class BrowserFactory {

    private static final Logger log = LogManager.getLogger(BrowserFactory.class);

    private BrowserFactory() {}

    public static WebDriver createDriver() {
        String browser = ConfigReader.getInstance().getBrowser().toLowerCase().trim();
        boolean headless = ConfigReader.getInstance().isHeadless();

        log.info("Opening browser: {} | Headless: {}", browser, headless);

        switch (browser) {
            case "chrome":  return createChrome(headless);
            case "firefox": return createFirefox(headless);
            case "edge":    return createEdge(headless);
            default:
                log.warn("Unknown browser '{}' - defaulting to Chrome", browser);
                return createChrome(headless);
        }
    }

    private static WebDriver createChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Disable password manager + weak password popup
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

        options.setExperimentalOption("prefs", prefs);

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        log.info("ChromeDriver is ready!");
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) options.addArguments("--headless");
        log.info("FirefoxDriver is ready!");
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdge(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        if (headless) options.addArguments("--headless=new");
        log.info("EdgeDriver is ready!");
        return new EdgeDriver(options);
    }
}