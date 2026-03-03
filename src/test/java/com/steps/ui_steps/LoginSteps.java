package com.steps.ui_steps;

import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

import com.bases.BaseTest;
import com.utils.TestDataUtil;

public class LoginSteps extends BaseTest {

    @Given("the user is on the Login page and the header is {string}")
    public void userIsOnLoginPage(String pageHeader) {
        // verify login page is displayed String header =
        String header = loginPage.getPageHeader();
        assert header != null && header.equalsIgnoreCase(pageHeader) :
            "Expected login page header '" + pageHeader + "', got: " + header;
    }

    @When("user logs in with test data from row {int} in CSV file {string}")
    public void userLogsInWithCsvData(int rowIndex, String fileName) {
        Map<String, String> testData = TestDataUtil.getCsvRecord(fileName, rowIndex);
        String username = testData.get("Username");
        String password = testData.get("Password");

        loginPage.setUsername(username);
        loginPage.setPassword(password);
    }

    @And("the user clicks the Login button")
    public void userClicksLoginButton() {
        loginPage.clickLoginPage();
    }

    @Then("the user should be redirected to the Product page and the header is {string}")
    public void productPageShouldbeDisplayed(String pageHeader) {
        String header = productPage.getProductPageHeader();
        assert header != null && header.equalsIgnoreCase(pageHeader) :
        "Expected product page '" + pageHeader + "',got: " +header;
        // toLowerCase().contains("product") : "Product page not shown, header=" + header;
    }

    @Then("the system must show an error message saying {string}")
    public void lockedOutError(String lockedOutErrorMessage) {
        String actual = loginPage.getLockedOutErrorMessage();
        assert actual != null && actual.equals(lockedOutErrorMessage) :
            "Expected error '" + lockedOutErrorMessage + "' but got '" + actual + "'";
    }


}
