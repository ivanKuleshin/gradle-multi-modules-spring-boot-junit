package org.example.test.testcases.addemployee;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.constants.CommonConstants.MESSAGE_FIELD;
import static org.example.utils.AssertionUtils.verifyStatusCode;

import io.restassured.http.Method;
import io.restassured.response.Response;
import java.util.Collections;
import org.example.clients.RestClient;
import org.example.model.Employee;
import org.example.test.testcases.BaseTest;
import org.example.utils.PropertiesReader;
import org.example.utils.testdataconverters.JsonFileConverter;
import org.junit.jupiter.api.Tag;
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

  @ParameterizedTest(name = "[{index}] {0}")
  @CsvFileSource(resources = TEST_DATA_PATH + "addEmployeePositiveTest.csv", numLinesToSkip = 1, delimiter = PIPE_DELIMITER)
  void addEmployeePositiveTest(String ignoredDescription,
                               @ConvertWith(JsonFileConverter.class) Employee employee,
                               String successfulMessage) {
    // Given
    mockitoClient.prepareTrueMockForNoRestClient();

    // When
    Response response = sendPutRequestToAddEmployee(employee);

    // Then
    mockitoClient.verifyMockForNoRestClient(employee);
    assertThat(response.statusCode()).as(response.asPrettyString()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.asString()).contains(format(successfulMessage, employee.getId()));
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @CsvFileSource(resources = TEST_DATA_PATH + "addEmployeeNegativeCase.csv", numLinesToSkip = 1, delimiter = PIPE_DELIMITER)
  void addEmployeeNegativeCase(String ignoredDescription,
                               @ConvertWith(JsonFileConverter.class) Employee employee,
                               String errorMessage,
                               Integer noRestClientInteractions) {
    // Given
    mockitoClient.prepareTrueMockForNoRestClient();
    employee.setId(null);

    // When
    Response response = sendPutRequestToAddEmployee(employee);

    // Then
    verifyStatusCode(response, HttpStatus.BAD_REQUEST.value());

    mockitoClient.verifyNoRestClientMockInteractions(noRestClientInteractions);
    assertThat(response.jsonPath().getString(MESSAGE_FIELD)).as(response.asPrettyString()).isEqualTo(errorMessage);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @CsvFileSource(resources = TEST_DATA_PATH + "addEmployeeWasNotAddedToExternalDb.csv", numLinesToSkip = 1, delimiter = PIPE_DELIMITER)
  void addEmployeeWasNotAddedToExternalDb(String ignoredDescription,
                                          @ConvertWith(JsonFileConverter.class) Employee employee,
                                          Integer employeeId,
                                          String errorMessage) {
    // Given
    employee.setId(employeeId);
    mockitoClient.prepareFalseMockForNoRestClient();

    // When
    Response response = sendPutRequestToAddEmployee(employee);

    // Then
    verifyStatusCode(response, HttpStatus.SERVICE_UNAVAILABLE.value());

    mockitoClient.verifyMockForNoRestClient(employee);
    assertThat(response.jsonPath().getString(MESSAGE_FIELD)).as(response.asPrettyString()).isEqualTo(errorMessage);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @CsvFileSource(resources = TEST_DATA_PATH + "addEmployeeNoRestClientException.csv", numLinesToSkip = 1, delimiter = PIPE_DELIMITER)
  void addEmployeeNoRestClientException(String ignoredDescription,
                                        @ConvertWith(JsonFileConverter.class) Employee employee,
                                        Integer employeeId,
                                        String mockErrorMessage,
                                        String errorMessage) {
    // Given
    employee.setId(employeeId);
    mockitoClient.prepareMockWithExceptionForNoRestClient(mockErrorMessage);

    // When
    Response response = sendPutRequestToAddEmployee(employee);

    // Then
    verifyStatusCode(response, HttpStatus.INTERNAL_SERVER_ERROR.value());

    mockitoClient.verifyMockForNoRestClient(employee);
    assertThat(response.jsonPath().getString(MESSAGE_FIELD)).as(response.asPrettyString()).isEqualTo(errorMessage);
  }

  private Response sendPutRequestToAddEmployee(Employee employee) {
    return RestClient.getInstance()
        .initializeSpecifications()
        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .sendRequestWithBody(Method.PUT, baseUrl + PropertiesReader.getInstance().getEmployeeBasePath(), Collections.emptyMap(), employee);
  }
}
