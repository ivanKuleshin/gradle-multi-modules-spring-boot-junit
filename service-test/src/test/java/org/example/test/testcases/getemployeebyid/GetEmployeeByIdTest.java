package org.example.test.testcases.getemployeebyid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.constants.EmployeeFields.EMPLOYEE_HASH_VALUE_FIELD;

import io.restassured.http.Method;
import io.restassured.response.Response;
import java.security.SecureRandom;
import java.util.Map;
import org.apache.commons.collections4.IterableUtils;
import org.example.ServiceClass;
import org.example.clients.RestClient;
import org.example.enums.RequestTypes;
import org.example.model.Employee;
import org.example.test.testcases.BaseTest;
import org.example.utils.PropertiesReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@Tag("parallel")
@Execution(ExecutionMode.CONCURRENT)
public class GetEmployeeByIdTest extends BaseTest {
  private static final String TEST_DATA_PATH = "/testdata/getemployeebyid/";
  private static final int START_INDEX = 0;

  @Value("${clients.externalClient.path}")
  private String externalClientPath;

  @Test
  public void test1() {
    ServiceClass serviceClass = new ServiceClass("TEST");
    Assertions.assertEquals(serviceClass.serviceClassFieldName, "TEST");
    System.out.println(serviceClass.serviceClassFieldName);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @CsvFileSource(resources = TEST_DATA_PATH + "getEmployeeByIdPositiveCase.csv", numLinesToSkip = 1, delimiter = PIPE_DELIMITER)
  void getEmployeeByIdPositiveCase(String ignoredDescription, String employeeHashValue, int externalClientRequestCount) {
    // Given
    Integer employeeId = defaultEmployeesList.get(new SecureRandom().nextInt(START_INDEX, defaultEmployeesList.size())).getId();

    wireMockClient.publishGetMapping(externalClientPath + employeeId, Map.of(EMPLOYEE_HASH_VALUE_FIELD, employeeHashValue));

    Employee expectedEmployee = IterableUtils.find(defaultEmployeesList, employee -> employee.getId().equals(employeeId));
    expectedEmployee.setEmployeeHash(employeeId + employeeHashValue);

    // When
    Response response = RestClient.getInstance()
        .initializeSpecifications()
        .sendRequestWithoutParams(Method.GET, baseUrl + PropertiesReader.getInstance().getEmployeeBasePath() + SLASH_DELIMITER + employeeId);

    // Then
    wireMockClient.verifyMapping(RequestTypes.GET, externalClientPath + employeeId, externalClientRequestCount);
    assertThat(response.statusCode()).as(response.asPrettyString()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.as(Employee.class)).isEqualTo(expectedEmployee);
  }

}
