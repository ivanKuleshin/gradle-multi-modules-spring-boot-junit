package org.example.clients;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.request;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.example.utils.JsonUtils.castObjectToJsonString;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.lang3.StringUtils;
import org.example.enums.RequestTypes;
import org.example.exceptions.TestExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class WireMockClient {

  private static final String INVALID_HTTP_METHOD_MESSAGE = "'%s' invalid HTTP method received!";

  public static void resetWireMock() {
    WireMock.reset();
  }

  public void publishGetMapping(String url, Object responseBody) {
    publishMapping(RequestTypes.GET, url, StringUtils.EMPTY, castObjectToJsonString(responseBody));
  }

  public void publishErrorMapping(RequestTypes requestType, String url, HttpStatus statusCode, Object responseBody) {
    switch (requestType) {
      case DELETE -> WireMock.stubFor(delete(url).willReturn(attachStatusCodeAndResponseBody(statusCode, responseBody)));
      case PUT -> WireMock.stubFor(put(url).willReturn(attachStatusCodeAndResponseBody(statusCode, responseBody)));
      case GET -> WireMock.stubFor(get(url).willReturn(attachStatusCodeAndResponseBody(statusCode, responseBody)));
      default -> throw new TestExecutionException(INVALID_HTTP_METHOD_MESSAGE, requestType);
    }
  }

  public void publishErrorMapping(RequestTypes requestType, String url, HttpStatus statusCode) {
    switch (requestType) {
      case DELETE -> WireMock.stubFor(delete(url).willReturn(aResponse().withStatus(statusCode.value())));
      case PUT -> WireMock.stubFor(put(url).willReturn(aResponse().withStatus(statusCode.value())));
      case GET -> WireMock.stubFor(get(url).willReturn(aResponse().withStatus(statusCode.value())));
      default -> throw new TestExecutionException(INVALID_HTTP_METHOD_MESSAGE, requestType);
    }
  }

  public void publishMapping(RequestTypes requestType, String url, String requestBody, String responseBody) {
    switch (requestType) {
      case PUT, POST -> WireMock
          .stubFor(request(requestType.getValue(), urlEqualTo(url))
              .withRequestBody(equalToJson(requestBody, true, false))
              .willReturn(okJson(responseBody)));
      case DELETE -> WireMock.stubFor(delete(url));
      case GET -> WireMock
          .stubFor(get(url)
              .willReturn(okJson(responseBody)));
      default -> throw new TestExecutionException(INVALID_HTTP_METHOD_MESSAGE, requestType);
    }
  }

  public void verifyMapping(RequestTypes requestType, String url, int count) {
    switch (requestType) {
      case DELETE -> WireMock.verify(count, deleteRequestedFor(urlEqualTo(url)));
      case PUT -> WireMock.verify(count, putRequestedFor(urlEqualTo(url)));
      case GET -> WireMock.verify(count, getRequestedFor(urlEqualTo(url)));
      case POST -> WireMock.verify(count, postRequestedFor(urlEqualTo(url)));
      default -> throw new TestExecutionException(INVALID_HTTP_METHOD_MESSAGE, requestType);
    }
  }

  private ResponseDefinitionBuilder attachStatusCodeAndResponseBody(HttpStatus statusCode, Object responseBody) {
    return aResponse().withStatus(statusCode.value())
        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .withBody(castObjectToJsonString(responseBody));
  }
}
