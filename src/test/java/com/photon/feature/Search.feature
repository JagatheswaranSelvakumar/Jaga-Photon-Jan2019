@SearchPage
Feature: As a product manager
  I want our users to be search the text
  So that they have a better experience

  #---------------------------------------------------------------------------------
  @SearchText
  Scenario: user validating google search page
    Given user launch the google application
    When user search "google" text 
    Then user verifies the search result page is displayed
