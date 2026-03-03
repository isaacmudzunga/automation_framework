package com.steps.ui_steps;

import java.io.IOException;

import com.bases.BaseTest;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks extends BaseTest{

    @Before
    public void setUp() throws IOException{
        initDriver();
    }

    @After
    public void tearDown() {
        quitDriver();
    }
}
