package com.steps.ui_steps;

import java.io.IOException;
import java.util.Map;

import io.cucumber.java.Before;
import io.cucumber.java.After;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

import com.bases.BaseTest;
import com.utils.TestDataUtil;

public class LoginSteps extends BaseTest {

    // @Before
    // public void setUp() throws IOException {
    //     initDriver();  // opens browser
    // }

    // @After
    // public void tearDown() {
    //     quitDriver();  // closes browser
    // }

    @Given("the user is on the Login page")
    public void userIsOnLoginPage() {
        // simply verify login page is displayed
        loginPage.getPageHeader();
    }

    @When("the user enters a valid username and password")
    public void userLogsInWithValidCredentials() {
        // read first row from the default CSV test data file
        Map<String, String> testData = TestDataUtil.getFirstRecord("e2e_test_data.csv");
        String username = testData.get("Username");
        String password = testData.get("Password");

        loginPage.setUsername(username);
        loginPage.setPassword(password);
    }

    @And("the user clicks the Login button")
    public void userClicksLoginButton() {
        loginPage.clickLoginPage();
    }

    @Then("the user should be redirected to the Product page")
    @And("the Product page should be displayed")
    public void productPageShouldbeDisplayed() {
        productPage.getProductPageHeader();
    }
}
