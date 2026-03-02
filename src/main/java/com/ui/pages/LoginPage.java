package com.ui.pages;

import org.openqa.selenium.By;

import com.ui.BasePage;

public class LoginPage extends BasePage{

    private static class LoginLocators {
        static final By pageHeader = By.cssSelector(".login_logo");
        static final By usernameField = By.cssSelector("[data-test='username']");
        static final By passwordField = By.id("password");
        static final By loginButton = By.id("login-button");
    }

    public String getPageHeader() {
        return waitForVisibility(LoginLocators.pageHeader).getText();
    }

    public void setUsername(String username) {
        waitForVisibility(LoginLocators.usernameField);
        set(LoginLocators.usernameField, username);
    }

    public void setPassword(String password) {
        waitForVisibility(LoginLocators.passwordField);
        set(LoginLocators.passwordField, password);
    }

    public ProductPage clickLoginPage() {
        waitForVisibility(LoginLocators.loginButton);
        click(LoginLocators.loginButton);
        return new ProductPage();
        
    }

    public ProductPage logIntoApplication(String username, String password) {
        setUsername(username);
        setPassword(password);
        return new ProductPage();
    }
}
