package com.steps.ui_steps;

import com.bases.BaseTest;
import com.utils.SequenceStepHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class loginSequenceStepDefinitions extends BaseTest {

    @Given("I execute the sequence from {string} using element list {string} and test data {string}")
    public void executeSequenceSteps(String sequenceCsv, String elementListCsv, String testDataCsv) {
        SequenceStepHelper helper = new SequenceStepHelper(driver);
        helper.runSequence(sequenceCsv, elementListCsv, testDataCsv, 0);
    }

    @Then("the user should be on the page titled {string}")
    public void verifyPageTitle(String expectedTitle) {
        String actualTitle = driver.getTitle();
        if (!actualTitle.equals(expectedTitle)) {
            throw new AssertionError("Expected page title: " + expectedTitle + " but got: " + actualTitle);
        }
    }
}
