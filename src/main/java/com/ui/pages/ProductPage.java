package com.ui.pages;

import org.openqa.selenium.By;

import com.ui.BasePage;

public class ProductPage extends BasePage{

    public static class ProductPageLocators {
        static final By pageHeader = By.cssSelector("[data-test='title']");
    }

    private By getProductTitleLocator(String productTitle) {
        return By.xpath("//div[normalize-space()='" + productTitle + "']");
    }

    private By getAddToCartButtonLocator(String productTitle) {
        return By.cssSelector("[data-test='add-to-cart-" + normalizeProductTitle(productTitle) + "']");
    }

    private By getRemoveButtonLocator(String productTitle) {
        return By.cssSelector("[data-test='remove-" + normalizeProductTitle(productTitle) + "']");
    }

    private By getProductDescriptionLocator(String productTitle) {
        return By.xpath("//div[@class='inventory_item' and .//div[contains(@class,'inventory_item_name') and normalize-space()='" + productTitle + "']]//div[@class='inventory_item_desc']");
    }

    private By getProductPriceLocator(String productTitle) {
        return By.xpath("//div[@class='inventory_item' and .//div[contains(@class,'inventory_item_name') and normalize-space()='" + productTitle + "']]//div[@class='inventory_item_price']");
    }

    private String normalizeProductTitle(String productTitle) {
        return productTitle.toLowerCase().replace(" ", "-");
    }

    public String getProductPageHeader() {
        return waitForPresence(ProductPageLocators.pageHeader).getText();
    }

    public String getProductTitle(String productTitle) {
        return waitForVisibility(getProductTitleLocator(productTitle)).getText().trim();
    }

    public String getProductPrice(String productTitle) {
        return waitForVisibility(getProductPriceLocator(productTitle)).getText().trim();
    }

    public void addProductToCart(String productTitle) {
        By addToCartButton = getAddToCartButtonLocator(productTitle);
        waitForVisibility(addToCartButton);
        click(addToCartButton);
    }

    public String getProductButtonText(String productTitle) {
        By removeButton = getRemoveButtonLocator(productTitle);
        if(isElementDisplayed(removeButton)) {
            return waitForVisibility(removeButton).getText().trim();
        }
        By addButton = getAddToCartButtonLocator(productTitle);
        return waitForVisibility(addButton).getText().trim();
    }

    public String getProductDescription(String productTitle) {
        return waitForVisibility(getProductDescriptionLocator(productTitle)).getText().trim();
    }

    public ProductDetailsPage getIntoProductDetails(String productTitle) {
        By clickProduct = getProductTitleLocator(productTitle);
        waitForVisibility(clickProduct);
        click(clickProduct);
        return new ProductDetailsPage();
    }

}
