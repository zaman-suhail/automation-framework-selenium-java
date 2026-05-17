package com.framework.pages;

import com.framework.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Template Method Pattern
 * Parent class for all page objects
 * Contains common actions every page can use
 */
public abstract class BasePage {

    protected static final Logger log = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    protected void click(By locator) {
        log.info("Click: {}", locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        log.info("Type '{}' into: {}", text, locator);
        WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
        el.clear();
        el.sendKeys(text);
    }

    protected String getText(By locator) {
        String text = wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        ).getText();
        log.debug("Got text '{}' from: {}", text, locator);
        return text;
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            log.debug("Element not found: {}", locator);
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public abstract boolean isPageLoaded();
}