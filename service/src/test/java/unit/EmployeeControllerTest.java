package unit;

import org.example.client.ExternalClient;
import org.example.controller.EmployeeController;
import org.example.exception.BadRequestException;
import org.example.model.Employee;
import org.example.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.builders.EmployeeBuilder.buildDefaultEmployee;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;
    @Mock
    private ExternalClient externalClient;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    public void setup() {
        employee1 = buildDefaultEmployee();
        employee2 = buildDefaultEmployee();
    }

    @Test
    public void testGetAllEmployees() {
        // Given
        when(employeeService.getAll()).thenReturn(Arrays.asList(employee1, employee2));

        // When
        List<Employee> result = employeeController.getAllEmployees();

        // Then
        assertEquals(2, result.size());
        assertEquals(employee1, result.get(0));
        assertEquals(employee2, result.get(1));
    }

    @Test
    public void testGetEmployeeById() {
        // Given
        when(employeeService.getById(1)).thenReturn(employee1);

        // When
        Employee result = employeeController.getById(1);

        // Then
        assertEquals(employee1, result);
    }

    @Test
    public void testUpdateExistingEmployee() {
        // Given
        when(employeeService.getById(employee1.getId())).thenReturn(employee1);
        employee1.setName("New Name");
        employee1.setEmployeeHash("Updated Hash");
        when(employeeService.update(employee1)).thenReturn(employee1);

        // When
        Employee result = employeeController.updateEmployee(employee1);

        // Then
        assertEquals(employee1, result);
        verify(employeeService, times(1)).getById(employee1.getId());
        verify(employeeService, times(1)).update(employee1);
    }

    @Test
    public void testUpdateNonExistingEmployee() {
        // Given
        when(employeeService.getById(employee2.getId())).thenReturn(null);

        // Then
        assertThrows(BadRequestException.class, () -> employeeController.updateEmployee(employee2));
    }

    @Test
    public void testRemoveEmployeeById() {
        // Given
        doNothing().when(employeeService).deleteById(1);

        // When
        String result = employeeController.removeEmployeeById(1);

        // Then
        assertEquals("Employee with id = 1 has been successfully deleted!", result);
    }

    @Test
    public void testDeleteAll() {
        // Given
        doNothing().when(employeeService).clear();

        // When
        String result = employeeController.deleteAll();

        // Then
        assertEquals("All employees have been successfully deleted!", result);
    }
}