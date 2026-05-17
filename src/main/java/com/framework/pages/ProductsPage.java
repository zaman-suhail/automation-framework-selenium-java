package com.framework.pages;

import org.openqa.selenium.By;

/**
 * ProductsPage - Page Object Model
 * This page appears after a successful login on saucedemo
 */
public class ProductsPage extends BasePage {

    // Locators
    private final By pageTitle    = By.className("title");
    private final By menuButton   = By.id("react-burger-menu-btn");
    private final By logoutLink   = By.id("logout_sidebar_link");
    private final By productsList = By.className("inventory_list");

    // Read the page heading - should say "Products"
    public String getProductsTitle() {
        return getText(pageTitle);
    }

    // Check if products list is visible
    public boolean isProductsListDisplayed() {
        return isDisplayed(productsList);
    }

    // Logout and return to LoginPage
    public LoginPage logout() {
        System.out.println("Logging out...");
        click(menuButton);
        click(logoutLink);
        return new LoginPage();
    }

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(productsList);
    }
}