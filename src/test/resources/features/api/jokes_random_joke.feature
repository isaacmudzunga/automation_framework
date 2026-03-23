Feature: Jokes API - Random Joke

    Scenario: Get a random joke successfully
        When I send a GET request to the random joke endpoint
        Then the response status code should be 200
        And the response content type should be JSON
        And the response should contain fields "id", "type", "setup", "punchline"
        And the field "setup" should not be empty
        And the field "punchline" should not be empty