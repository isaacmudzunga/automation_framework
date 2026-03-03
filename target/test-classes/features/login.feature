Feature: Login functionality

As a registered user
I want to log into the application
So that I can access the product page

Background:
Given the user is on the Login page

Scenario: Successful login with valid credentials
When the user enters a valid username and password
And the user clicks the Login button
Then the user should be redirected to the Product page
And the Product page should be displayed