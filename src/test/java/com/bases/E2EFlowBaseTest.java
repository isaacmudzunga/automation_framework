package com.bases;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

public class E2EFlowBaseTest extends BaseTest {
    @BeforeClass
    public void setUp() throws IOException {
        initDriver();
    }

    @AfterMethod
    public void captureFailure(ITestResult result) {
        takeScreenshotOnFailure(result);
    }

    @AfterClass
    public void tearDown() {
        quitDriver();
    }
}
