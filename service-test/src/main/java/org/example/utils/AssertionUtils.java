package org.example.utils;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.Response;

public class AssertionUtils {

  private AssertionUtils() {
  }

  /**
   * It's better to validate status code before mocks and response verification. It allows to achieve the most correct flow o verification.
   * Ideally, that flow should look like: verify status code -> verify interactions with mock -> verify mocks -> verify response from the service
   */
  public static void verifyStatusCode(Response response, int expectedStatusCode) {
    assertThat(response.statusCode()).as(response.asPrettyString()).isEqualTo(expectedStatusCode);
  }
}
