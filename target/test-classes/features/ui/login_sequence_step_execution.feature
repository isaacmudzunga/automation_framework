@ui @sequence
Feature: Execute UI steps from CSV sequence

  Scenario: Login using CSV-driven element sequence
    Given I execute the sequence from "webelementsequencesteps/login_sequence_steps_template.csv" using element list "webdriverelements/loginPage_element_list_template.csv" and test data "testdata/test_data_template.csv"
    # Then the user should be on the page titled "Products"
