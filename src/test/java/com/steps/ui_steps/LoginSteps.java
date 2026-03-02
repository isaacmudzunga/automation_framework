package com.steps.ui_steps;

import java.io.IOException;
import java.util.Map;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bases.BaseTest;
import com.utils.TestDataUtil;

public class LoginSteps extends BaseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);
    
    @Before
    public void setUp() throws IOException {
        logger.info("=== Starting Login Test Setup ===");
        initDriver();
    }

    @After
    public void tearDown() {
        logger.info("=== Ending Login Test Teardown ===");
        quitDriver();
    }

    /**
     * Step: User logs in with test data from CSV file
     */
    @When("user logs in with test data from row {int} in CSV file {string}")
    public void userLogsInWithTestDataFromCSV(int rowIndex, String fileName) {        
        Map<String, String> testData = TestDataUtil.getCsvRecord(fileName, rowIndex);
        String username = testData.get("Username");
        String password = testData.get("Password");
        
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        productPage = loginPage.clickLoginPage();
    }

}
