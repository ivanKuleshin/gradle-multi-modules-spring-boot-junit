package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.client.NoRestClient;
import org.example.exception.BadRequestException;
import org.example.exception.CustomRuntimeException;
import org.example.exception.NotFoundException;
import org.example.exception.ServiceUnavailableException;
import org.example.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private NoRestClient noRestClient;
    @Autowired
    private EmployeeMapService employeeMapService;

    public List<Employee> getAll() {
        return employeeMapService.getAll()
                .stream()
                .sorted(Comparator.comparing(Employee::getId))
                .collect(Collectors.toList());
    }

    public Employee getById(Integer id) {
        checkKey(id);
        return employeeMapService.getById(id, null);
    }

    public Employee update(Employee employee) {
        if (isNull(employee) || isNull(employee.getId())) {
            throw new CustomRuntimeException("Employee cannot be null");
        } else if (!employeeMapService.containsKey(employee.getId())) {
            throw new CustomRuntimeException(String.format("Employee with such id = %s doesn't  exist", employee.getId()));
        } else {
            employeeMapService.add(employee);
        }
        return employee;
    }

    public void add(Employee employee) {
        checkObjectIsNull(employee);
        checkObjectIsNull(employee.getId());

        if (employeeMapService.containsKey(employee.getId())) {
            throw new BadRequestException(String.format("Employee with such id = %s already exists", employee.getId()));
        } else {
            employeeMapService.add(employee);

            log.info("Employee with id {} is going to be saved into external DB", employee.getId());
            if (noRestClient.saveEmployeeToExternalDb(employee)) {
                log.info("Employee with id {} is saved to the external DB!", employee.getId());
            } else {
                throw new ServiceUnavailableException("Employee was not saved to the external DB!");
            }
        }
    }

    public void addList(List<Employee> employees) {
        if (employees.isEmpty()) {
            throw new CustomRuntimeException("No employees to add!");
        }

        employees.forEach(employee -> {
            if (employeeMapService.containsKey(employee.getId())) {
                throw new CustomRuntimeException(String
                        .format("Employee with such id = %s already exists", employee.getId()));
            } else {
                employeeMapService.add(employee);
            }

        });
    }

    public void deleteById(Integer id) {
        checkKey(id);
        employeeMapService.deleteById(id);
    }

    public void clear() {
        employeeMapService.clear();
    }

    private void checkKey(Integer id) {
        if (isNull(id)) {
            throw new BadRequestException("Employee id cannot be null");
        } else if (!employeeMapService.containsKey(id)) {
            throw new NotFoundException(String
                    .format("Employee with such id = %s not found", id));
        }
    }

    private void checkObjectIsNull(Object employee) {
        if (isNull(employee)) {
            throw new BadRequestException("Employee cannot be null");
        }
    }
}