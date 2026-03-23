package com.steps.ui_steps;

import com.bases.BaseTest;
import io.cucumber.java.en.*;
import java.util.Map;

import com.utils.TestDataUtil;

public class LoginSteps extends BaseTest{

    @Given("the user is logged in as a standard user")
    public void userLogsWithStandardUser() {
        Map<String, String> testData = TestDataUtil.getCsvRecord("e2e_test_data.csv", 0);
        String username = testData.get("Username");
        String password = testData.get("Password");

        if (username == null || password == null) {
            throw new RuntimeException("Username or Password missing in CSV file.");
        }

        loginPage.logIntoApplication(username, password);
        loginPage.clickLoginPage();
    }

    @Then("the user should be on the page titled {string}")
    public void productPageShouldbeDisplayed(String pageHeader) {
        String header = productPage.getProductPageHeader();
        assert header != null && header.equalsIgnoreCase(pageHeader) :
        "Expected product page '" + pageHeader + "',got: " +header;
    }

}
