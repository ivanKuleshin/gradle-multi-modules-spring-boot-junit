package unit;

import org.example.client.NoRestClient;
import org.example.exception.BadRequestException;
import org.example.exception.ServiceUnavailableException;
import org.example.model.Employee;
import org.example.service.EmployeeMapService;
import org.example.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static utils.builders.EmployeeBuilder.buildDefaultEmployee;
import static utils.builders.EmployeeBuilder.buildNullEmployee;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceAddMethodNegativeTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeMapService employeeMapService;
    @Mock
    NoRestClient noRestClient;

    @Test
    public void testAddNullEmployee() {
        // Given
        Employee nullEmployee = buildNullEmployee();

        // Then
        assertThrows(BadRequestException.class, () -> employeeService.add(nullEmployee));
    }

    @Test
    public void testAddNullEmployeeId() {
        // Given
        Employee employeeWithNullId = buildDefaultEmployee();
        employeeWithNullId.setId(null);

        // Then
        assertThrows(BadRequestException.class, () -> employeeService.add(employeeWithNullId));
    }

    @Test
    public void testAddEmployeeAlreadyExists() {
        // Given
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1);
        when(employeeMapService.containsKey(1)).thenReturn(true);

        // Then
        assertThrows(BadRequestException.class, () -> employeeService.add(existingEmployee));
    }

    @Test
    public void testAddFailureInExternalService() {
        // Given
        Employee newEmployee = new Employee();
        newEmployee.setId(1);
        when(employeeMapService.containsKey(1)).thenReturn(false);
        when(noRestClient.saveEmployeeToExternalDb(newEmployee)).thenReturn(false);

        // Then
        assertThrows(ServiceUnavailableException.class, () -> employeeService.add(newEmployee));
    }
}