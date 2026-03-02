package com.ui.pages;

import org.openqa.selenium.By;

import com.ui.BasePage;

public class ProductPage extends BasePage{

    public static class ProductPageLocators {
        static final By pageHeader = By.cssSelector(".app_logo"); 
    }

    public String getProductPageHeader() {
        return waitForPresence(ProductPageLocators.pageHeader).getText();
    }
}
