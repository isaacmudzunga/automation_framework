package com.steps.ui_steps;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import com.bases.BaseTest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

public class ProductsSteps extends BaseTest{

    @When("the user adds the product {string} to the cart")
    public void addPackToCart(String productTitle) {
        String title = productPage.getProductTitle(productTitle);
        assert title != null && title.equalsIgnoreCase(productTitle) :
        "Expected product page '" + productTitle + "',got: " +title;

        productPage.addProductToCart(productTitle);
    }

    @And("the product {string} should have the action button {string}")
    public void getBackPackButtonStatusChange(String productTitle, String buttonStatus) {
        String title = productPage.getProductTitle(productTitle);
        assert title != null && title.equalsIgnoreCase(productTitle) :
        "Expected product page '" + productTitle + "',got: " +title;

        String status = productPage.getProductButtonText(productTitle);
        assert status != null && status.equalsIgnoreCase(buttonStatus) :
        "Expected product page '" + buttonStatus + "',got: " +status;
    }

    @And("the product {string} should have the price {string}")
    public void getBackPackPrice(String productTitle, String productPrice) {
        String title = productPage.getProductTitle(productTitle);
        assert title != null && title.equalsIgnoreCase(productTitle) :
        "Expected product page '" + productTitle + "',got: " +title;

        productPage.getProductPrice(productTitle);
    }

    @When("the user opens the product details page for {string}")
    public void getIntoProductDetails(String productTitle) {
        productPage.getIntoProductDetails(productTitle);
    }

    @Then("Then the product details should show")
    public void productPageShouldShow(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        Map<String, String> productData = rows.get(0);

        String expectedName = productData.get("product_name");
        String expectedDescription = productData.get("descrition");
        String expectedPrice = productData.get("price");

        String actualName = productPage.getProductTitle(expectedName);
        String actualDescription = productPage.getProductTitle(expectedDescription);
        String actualPrice = productPage.getProductTitle(expectedPrice);

        assertEquals(actualName, expectedName);
        assertEquals(actualDescription, expectedDescription);
        assertEquals(actualPrice, expectedPrice);
    }

}
