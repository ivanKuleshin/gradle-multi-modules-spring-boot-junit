package org.example.service;

import org.example.exception.CustomRuntimeException;
import org.example.model.Employee;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class EmployeeService {

    private final ConcurrentHashMap<Integer, Employee> employeeMap = new ConcurrentHashMap<>();

    public List<Employee> getAll() {
        return employeeMap.values()
                .stream().sorted(Comparator.comparing(Employee::getId))
                .collect(Collectors.toList());
    }

    public Employee getById(Integer id) {
        checkKey(id);
        return employeeMap.getOrDefault(id, null);
    }

    public Employee update(Employee employee) {
        if (isNull(employee) || isNull(employee.getId())) {
            throw new CustomRuntimeException("Employee cannot be null");
        } else if (!employeeMap.containsKey(employee.getId())) {
            throw new CustomRuntimeException(String
                    .format("Employee with such id = %s doesn't  exist", employee.getId()));
        } else {
            employeeMap.put(employee.getId(), employee);
        }
        return employee;
    }

    public void add(Employee employee) {
        if (isNull(employee) || isNull(employee.getId())) {
            throw new CustomRuntimeException("Employee cannot be null");
        } else if (employeeMap.containsKey(employee.getId())) {
            throw new CustomRuntimeException(String
                    .format("Employee with such id = %s already exists", employee.getId()));
        } else {
            employeeMap.put(employee.getId(), employee);
        }
    }

    public void addList(List<Employee> employees) {
        if (employees.isEmpty()) {
            throw new CustomRuntimeException("No employees to add!");
        }

        employees.forEach(employee -> {
            if (employeeMap.containsKey(employee.getId())) {
                throw new CustomRuntimeException(String
                        .format("Employee with such id = %s already exists", employee.getId()));
            } else {
                employeeMap.put(employee.getId(), employee);
            }

        });
    }

    public void deleteById(Integer id) {
        checkKey(id);
        employeeMap.remove(id);
    }

    public void clear() {
        employeeMap.clear();
    }

    private void checkKey(Integer id) {
        if (isNull(id)) {
            throw new CustomRuntimeException("id cannot be null");
        } else if (!employeeMap.containsKey(id)) {
            throw new CustomRuntimeException(String
                    .format("Employee with such id = %s not found", id));
        }
    }
}