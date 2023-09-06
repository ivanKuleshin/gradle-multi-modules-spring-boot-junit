package org.example.client;

import org.example.model.Employee;
import org.springframework.stereotype.Component;

/**
 * This class emulate third-party dependency which cannot be mocked via WireMock
 * To mock this client Mockito will be used
 */
@Component
public class NoRestClient {

  /**
   * This method logic should be mocked via Mockito
   */
  public boolean saveEmployeeToExternalDb(Employee employee) {
    Employee externalEmployee = Employee.builder()
        .id(employee.getId())
        .name(employee.getName())
        .employeeHash(employee.getEmployeeHash())
        .build();

    return !employee.equals(externalEmployee);
  }

}
