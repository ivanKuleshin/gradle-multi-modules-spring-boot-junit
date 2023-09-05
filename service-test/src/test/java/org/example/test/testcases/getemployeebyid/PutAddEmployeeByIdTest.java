package org.example.test.testcases.getemployeebyid;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.http.Method;
import io.restassured.response.Response;
import java.util.Collections;
import org.example.clients.RestClient;
import org.example.model.Employee;
import org.example.test.testcases.BaseTest;
import org.example.utils.testdataconverters.JsonFileConverter;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Tag("sequence")
@Execution(ExecutionMode.SAME_THREAD)
public class PutAddEmployeeByIdTest extends BaseTest {
  private static final String TEST_DATA_PATH = "/testdata/addemployee/";

  @Test
  void example() {
    System.out.println("Hello world!");
  }

  @ParameterizedTest
  @CsvFileSource(resources = TEST_DATA_PATH + "addEmployeePositiveTest.csv", numLinesToSkip = 1, delimiter = PIPE_DELIMITER)
  void addEmployeePositiveTest(@ConvertWith(JsonFileConverter.class) Employee employee) {
    // Given
    String successfulMessage = "Employee with id = %s has been added!";

    // When
    Response response = RestClient.getInstance()
        .initializeSpecifications()
        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .sendRequestWithBody(Method.PUT, baseUrl + "/employee", Collections.emptyMap(), employee);

    // Then
    assertThat(response.statusCode()).as(response.asPrettyString()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.asString()).contains(format(successfulMessage, employee.getId()));
  }
}
