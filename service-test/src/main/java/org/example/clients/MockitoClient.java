package org.example.clients;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.openMocks;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.example.client.NoRestClient;
import org.example.exceptions.TestExecutionException;
import org.example.model.Employee;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockitoClient {

  @Autowired
  private NoRestClient noRestClient;

  @Captor
  private ArgumentCaptor<Employee> employeeCaptor;

  @PostConstruct
  void init() {
    try (AutoCloseable ignored = openMocks(this)) {
      log.info("Mock initialization...");
    } catch (Exception e) {
      throw new TestExecutionException("Error during mock initialization. Cause: %s", e.getMessage());
    }
  }

  public void prepareTrueMockForNoRestClient() {
    Mockito.when(noRestClient.saveEmployeeToExternalDb(employeeCaptor.capture())).thenReturn(true);
  }

  public void prepareFalseMockForNoRestClient() {
    Mockito.when(noRestClient.saveEmployeeToExternalDb(employeeCaptor.capture())).thenReturn(false);
  }

  public void prepareMockWithExceptionForNoRestClient(String message) {
    Mockito.when(noRestClient.saveEmployeeToExternalDb(employeeCaptor.capture()))
        .thenThrow(new RuntimeException(message));
  }

  public void verifyMockForNoRestClient(Employee expectedEmployee) {
    Employee actualEmployee = employeeCaptor.getValue();

    Assertions.assertThat(actualEmployee).isEqualTo(expectedEmployee);
  }

  public void verifyNoRestClientMockInteractions(int count) {
    Mockito.verify(noRestClient, times(count)).saveEmployeeToExternalDb(any());
  }

  public void resetMocks() {
    Mockito.reset(noRestClient);
  }

  public void resetCaptures() {
    employeeCaptor = ArgumentCaptor.forClass(Employee.class);
  }
}
