Feature: Testing the Test REST API
  Users should be able to submit GET and POST requests to a web service, represented by WireMock

  Scenario: Get Test data '1'
    Given test data is present
    When users sends get request
    Then the server should return test with status success