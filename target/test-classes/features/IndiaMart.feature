Feature: IndiaMart Website Automation
  As a QA Engineer
  I want to automate IndiaMart test cases
  So that I can verify key functionalities of the website

  # Background runs BEFORE every scenario.
  # Each scenario gets a fresh browser session (handled by @Before in Hooks.java).
  Background:
    Given I open the IndiaMart website

  # ─────────────────────────────────────────────────────────────────────
  # TC01 – Top 3 Car Wash Services from Chennai
  # Tags used for grouping and selective execution:
  #   mvn test -Dcucumber.filter.tags="@TC01"
  # ─────────────────────────────────────────────────────────────────────
  @TC01 @smoke @carwash
  Scenario: Display top 3 car wash services from Chennai
    When I set the location to the configured city
    And I search for the configured keyword
    Then the top 3 service names should be displayed
    And the details should be saved to Excel

  # ─────────────────────────────────────────────────────────────────────
  # TC02 – Food, Agriculture & Farming Section
  # ─────────────────────────────────────────────────────────────────────
  @TC02 @homepage
  Scenario: Get product names from Food Agriculture and Farming section
    When I scroll to the Food Agriculture and Farming section
    Then all product names in that section should be displayed

  # ─────────────────────────────────────────────────────────────────────
  # TC03 – Invalid Phone Number Sign-In
  # ─────────────────────────────────────────────────────────────────────
  @TC03 @smoke @login
  Scenario: Sign in attempt with invalid phone number
    When I open the sign in dialog
    And I enter the invalid phone number from config
    And I submit the phone number
    Then a validation error message should be displayed
    And a screenshot of the error should be saved

  # ─────────────────────────────────────────────────────────────────────
  # TC04 – Top Product Categories from Exporters
  # ─────────────────────────────────────────────────────────────────────
  @TC04 @exporters
  Scenario: Capture top product categories from Exporters section
    When I navigate to the Exporters section
    Then the top product categories from India should be displayed
    And a screenshot of the categories should be saved
