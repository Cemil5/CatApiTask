@wip
Feature: CatApi Task

  Scenario: get all votes
    When the user gets votes information using token
    Then status code should be "200"
    And length of the response result is more than 0

  Scenario: getting a random vote
    Given the user gets all votes
    When the user selects a random element
    Then status code should be "200"
    And length of the response result is more than 0
    And all field in response object match the selected element

  Scenario: posting a new vote
    When the user create a new vote with image id "asf2" and sub id "my-user-1234" and value "1"
    Then status code should be "200"
    And body response has "SUCCESS" message
    And id respones is not empty

    Scenario: deleting a vote which api has
      Given the user creates a new vote and gets its id
      When the user deletes this id
      Then body response has "SUCCESS" message

      Scenario:deleting a vote which api doesn't have
        Given the user deletes a vote and keeps its id
        When the user tries to delete this id again
        Then body response has "NOT_FOUND" message
        Then status code should be "404"



