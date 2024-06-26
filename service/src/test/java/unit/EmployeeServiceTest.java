package unit;

import org.example.client.NoRestClient;
import org.example.model.Employee;
import org.example.service.EmployeeMapService;
import org.example.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.builders.EmployeeBuilder.buildDefaultEmployee;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeMapService employeeMapService;
    @Mock
    private NoRestClient noRestClient;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    public void setup() {
        Mockito.reset(employeeMapService, noRestClient);
        employee1 = buildDefaultEmployee();
        employee2 = buildDefaultEmployee();
    }

    @Test
    public void testGetAll() {
        // Given
        when(employeeMapService.getAll()).thenReturn(Arrays.asList(employee1, employee2));

        // When
        List<Employee> result = employeeService.getAll();

        // Then
        assertEquals(2, result.size());
        assertEquals(employee1, result.get(0));
        assertEquals(employee2, result.get(1));
    }

    @Test
    public void testGetById() {
        // Given
        when(employeeMapService.containsKey(1)).thenReturn(true);
        when(employeeMapService.getById(1, null)).thenReturn(employee1);

        // When
        Employee result = employeeService.getById(1);

        // Then
        assertEquals(employee1, result);
    }

    @Test
    public void testUpdate() {
        // Given
        when(employeeMapService.containsKey(employee1.getId())).thenReturn(true);

        // When
        Employee result = employeeService.update(employee1);

        // Then
        assertEquals(employee1, result);
    }

    @Test
    public void testAdd() {
        // Given
        Employee employee = new Employee();
        employee.setId(1);
        when(noRestClient.saveEmployeeToExternalDb(employee)).thenReturn(true);
        when(employeeMapService.containsKey(1)).thenReturn(false);

        // When
        employeeService.add(employee);

        // Then
        verify(employeeMapService, times(1)).add(employee);
    }

    @Test
    public void testAddList() {
        // Given
        when(employeeMapService.containsKey(employee1.getId())).thenReturn(false);
        when(employeeMapService.containsKey(employee2.getId())).thenReturn(false);

        // When
        employeeService.addList(Arrays.asList(employee1, employee2));

        // Then
        verify(employeeMapService, times(1)).add(employee1);
        verify(employeeMapService, times(1)).add(employee2);
    }

    @Test
    public void testDeleteById() {
        // Given
        when(employeeMapService.containsKey(1)).thenReturn(true);

        // When
        employeeService.deleteById(1);

        // Then
        verify(employeeMapService, times(1)).deleteById(1);
    }

    @Test
    public void testClear() {
        // When
        employeeService.clear();

        // Then
        verify(employeeMapService, times(1)).clear();
    }
}