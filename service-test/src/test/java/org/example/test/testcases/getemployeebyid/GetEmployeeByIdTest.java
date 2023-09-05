package org.example.test.testcases.getemployeebyid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.http.Method;
import io.restassured.response.Response;
import java.security.SecureRandom;
import org.apache.commons.collections4.IterableUtils;
import org.example.ServiceClass;
import org.example.clients.RestClient;
import org.example.model.Employee;
import org.example.test.testcases.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.http.HttpStatus;

@Tag("parallel")
@Execution(ExecutionMode.CONCURRENT)
public class GetEmployeeByIdTest extends BaseTest {
  private static final String TEST_DATA_PATH = "/testdata/getemployeebyid/";

  @Test
  public void test1() {
    ServiceClass serviceClass = new ServiceClass("TEST");
    Assertions.assertEquals(serviceClass.serviceClassFieldName, "TEST");
    System.out.println(serviceClass.serviceClassFieldName);
  }

  // TODO: add mocks for passing
  @Test
  void getEmployeeById() {
    // Given
    Integer employeeId = defaultEmployeesList.get(new SecureRandom().nextInt(0, defaultEmployeesList.size())).getId();

    Employee expectedEmployee = IterableUtils.find(defaultEmployeesList, employee -> employee.getId().equals(employeeId));

    // When
    Response response = RestClient.getInstance()
        .initializeSpecifications()
        .sendRequestWithoutParams(Method.GET, baseUrl + "/employee/" + employeeId);

    // Then
    assertThat(response.statusCode()).as(response.asPrettyString()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.as(Employee.class)).isEqualTo(expectedEmployee);
  }

}
