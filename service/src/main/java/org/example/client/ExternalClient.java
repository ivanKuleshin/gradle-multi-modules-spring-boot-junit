package org.example.client;

import io.restassured.response.Response;
import org.example.exception.ServiceUnavailableException;
import org.example.model.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.restassured.RestAssured.given;

@Component
public class ExternalClient {

  @Value("${clients.externalClient.url}")
  private String url;

  public void deleteRequestToExternalService(Integer id) {
    given().log().all().delete(url + id).then().statusCode(200);
  }

  public void getAllEmployees() {
    given().log().all().get(url).then().statusCode(200);
  }

  public String getEmployeeHash(Integer employeeId) {
    Response response = given().log().all().get(url + employeeId);

    if (response.getStatusCode() != HttpStatus.OK.value()) {
      throw new ServiceUnavailableException(String.format("External Client unavailable. Response code is: %s", response.statusCode()));
    } else {
      String hash = response.jsonPath().getString("employeeHashValue");
      return Optional.ofNullable(hash).orElse("").isEmpty() ? null : employeeId + hash;
    }
  }

  public Address addAddress(Integer employeeId, Address address) {
    return given().log().all().body(address)
        .post(url + "address/" + employeeId).jsonPath().getObject("$", Address.class);
  }
}
