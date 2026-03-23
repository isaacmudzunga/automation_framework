@ui @standard_user
Feature: Standard user shopping journey

As a standard user
I want to log in, add products to the cart, complete checkout, and log out
So that I can successfully purchase items in the application

Background:
Given the user is logged in as a standard user

@smoke @login
Scenario: Standard user logs in successfully
Then the user should be on the page titled "Products"

@cart
Scenario: Standard user adds a product to the cart from the products page
When the user adds the product "Sauce Labs Backpack" to the cart
And the product "Sauce Labs Backpack" should have the action button "Remove"
And the product "Sauce Labs Backpack" should have the price "$29.99"

@cart @product_details
Scenario: Standard user adds a product to the cart from the product details page
When the user opens the product details page for "Sauce Labs Fleece Jacket"
Then the product details should show
    | product_name             | description                                                                                                                                   | price  |
    | Sauce Labs Fleece Jacket | It's not every day that you come across a midweight quarter-zip fleece jacket capable of handling everything from a relaxing day outdoors to a busy day at the office. | $49.99 |
# When the user adds the current product to the cart
# Then the current product should have the action button "Remove"
# Then the user returns to the products page titled "Products"

# @cart
# Scenario: Standard user views selected products in the cart
# Given the user is logged in as a standard user
# And the user has added the following products to the cart:
#     | product_name             |
#     | Sauce Labs Backpack      |
#     | Sauce Labs Fleece Jacket |
# When the user opens the cart
# Then the user should be on the page titled "Your Cart"
# And the cart should contain the following products:
#     | product_name             | description                                                                                                                                   | price  |
#     | Sauce Labs Backpack      | carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.       | $29.99 |
#     | Sauce Labs Fleece Jacket | It's not every day that you come across a midweight quarter-zip fleece jacket capable of handling everything from a relaxing day outdoors to a busy day at the office. | $49.99 |

# @checkout
# Scenario: Standard user completes checkout successfully
# Given the user is logged in as a standard user
# And the user has added the following products to the cart:
#     | product_name             |
#     | Sauce Labs Backpack      |
#     | Sauce Labs Fleece Jacket |
# When the user proceeds to checkout
# Then the user should be on the page titled "Checkout: Your Information"
# When the user enters valid checkout information
# And the user continues checkout
# Then the user should be on the page titled "Checkout: Overview"
# And the checkout overview should contain the following products:
#     | product_name             | description                                                                                                                                   | price  |
#     | Sauce Labs Backpack      | carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.       | $29.99 |
#     | Sauce Labs Fleece Jacket | It's not every day that you come across a midweight quarter-zip fleece jacket capable of handling everything from a relaxing day outdoors to a busy day at the office. | $49.99 |
# And the payment information should be "SauceCard #31337"
# And the shipping information should be "Free Pony Express Delivery!"
# And the item total should be "$79.98"
# And the tax should be "$6.40"
# And the total should be "$86.38"
# When the user confirms the order
# Then the user should be on the page titled "Checkout: Complete!"
# And the order confirmation should show:
#     | message_type | value                                                                                           |
#     | success      | Thank you for your order!                                                                       |
#     | dispatch     | Your order has been dispatched, and will arrive just as fast as the pony can get there!        |
# When the user returns to the products page
# Then the user should be on the page titled "Products"

# @logout
# Scenario: Standard user logs out successfully
# Given the user is logged in as a standard user
# When the user logs out
# Then the user should be on the Login page
# And the page header should be "Swag Labs"

# @e2e @smoke
# Scenario: Standard user completes the full shopping journey
# Given the user is logged in as a standard user
# When the user adds the following products to the cart:
#     | product_name             |
#     | Sauce Labs Backpack      |
#     | Sauce Labs Fleece Jacket |
# And the user completes checkout with valid information
# Then the user should be on the page titled "Checkout: Complete!"
# And the order confirmation should show:
#     | message_type | value                                                                                           |
#     | success      | Thank you for your order!                                                                       |
#     | dispatch     | Your order has been dispatched, and will arrive just as fast as the pony can get there!        |
# When the user returns to the products page
# And the user logs out
# Then the user should be on the Login page
# And the page header should be "Swag Labs"