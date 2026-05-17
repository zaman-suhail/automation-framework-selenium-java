package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * LoginPage - Page Object Model + Fluent Interface
 * Contains all locators and actions for the login page
 * Each method returns LoginPage (this) to enable method chaining
 */
public class LoginPage extends BasePage {

    // Locators - actual elements from saucedemo
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton   = By.id("login-button");
    private final By errorMessage  = By.cssSelector("[data-test='error']");

    public LoginPage enterUsername(String username) {
        log.info("Entering username: {}", username);
        if (!username.isEmpty()) {
            type(usernameField, username);
        }
        return this;
    }

    public LoginPage enterPassword(String password) {
        log.info("Entering password");
        if (!password.isEmpty()) {
            type(passwordField, password);
        }
        return this;
    }

    // Click login button - Fluent
    public LoginPage clickLogin() {
        System.out.println("Clicking login button");
        click(loginButton);
        return this;
    }

    // Full login flow - returns ProductsPage on success
    public ProductsPage loginSuccessfully(String username, String password) {
        enterUsername(username)
                .enterPassword(password)
                .clickLogin();
        return new ProductsPage();
    }

    // Read error message text
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    // Check if error message is visible
    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    @Override
    public boolean isPageLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            boolean urlOk = wait.until(d ->
                    d.getCurrentUrl().contains("saucedemo")
            );

            boolean uiOk = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(loginButton)
            ).isDisplayed();

            return urlOk && uiOk;

        } catch (Exception e) {
            return false;
        }
    }
}