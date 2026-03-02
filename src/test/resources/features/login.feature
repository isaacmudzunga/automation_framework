Feature: Login Functionality
As a user
I want to login to the application
So that I can access the product page

Background:
Given user is on the login page

Scenario: Successful login using first row from CSV
When user logs in with test data from row 0 in CSV file "e2e_test_data.csv"
Then user should see the product page
