package org.example.service;

import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.model.Employee;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

@Service
public class EmployeeMapService {

    private final ConcurrentHashMap<Integer, Employee> employeeMap = new ConcurrentHashMap<>();

    public Collection<Employee> getAll() {
        return employeeMap.values();
    }

    public Employee getById(Integer id) {
        checkKey(id);
        return employeeMap.getOrDefault(id, null);
    }

    public Employee getById(Integer id, Employee defaultValue) {
        checkKey(id);
        return employeeMap.getOrDefault(id, defaultValue);
    }

    public void add(Employee employee) {
        checkObjectIsNull(employee);
        checkObjectIsNull(employee.getId());

        if (employeeMap.containsKey(employee.getId())) {
            throw new BadRequestException(String.format("Employee with such id = %s already exists", employee.getId()));
        } else {
            employeeMap.put(employee.getId(), employee);
        }
    }

    // containsKey method
    public boolean containsKey(Integer id) {
        return employeeMap.containsKey(id);
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
            throw new BadRequestException("Employee id cannot be null");
        } else if (!employeeMap.containsKey(id)) {
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
