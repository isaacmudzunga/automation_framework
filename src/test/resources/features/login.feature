Feature: Login functionality

As a registered user
I want to log into the application
So that I can access the product page

Background:
Given the user is on the Login page and the header is "Swag Labs"

Scenario: Successful login with a registered user
When user logs in with test data from row 0 in CSV file "e2e_test_data.csv"
And the user clicks the Login button
Then the user should be redirected to the Product page and the header is "Swag Labs"

Scenario: Unsuccessful login with a locked out user
When user logs in with test data from row 1 in CSV file "e2e_test_data.csv"
And the user clicks the Login button
Then the system must show an error message saying "Epic sadface: Sorry, this user has been locked out."

