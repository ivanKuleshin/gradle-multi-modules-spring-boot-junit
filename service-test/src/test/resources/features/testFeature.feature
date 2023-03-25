@test
Feature: test

  Scenario: test
#    configure WireMock
    And the 'STUB_REQUEST' variable is initialized in test session with "null" value
    And the 'STUB_RESPONSE' variable is initialized in test session with "null" value
    And wiremock stub is set for GET request with "/externalClient/" URL

    Given User performs test