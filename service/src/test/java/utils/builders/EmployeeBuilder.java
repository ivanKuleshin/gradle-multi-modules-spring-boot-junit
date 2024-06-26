package utils.builders;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.Employee;

public class EmployeeBuilder {

    public static Employee buildDefaultEmployee() {
        Employee employee = Employee.builder()
                .id(generateId())
                .name("John Doe")
                .passportNumber(RandomStringUtils.randomNumeric(6))
                .education("Master")
                .address(EmployeeAddressBuilder.buildDefaultAddress())
                .build();
        employee.generateEmployeeHash();
        return employee;
    }

    public static Employee buildNullEmployee() {
        return null;
    }

    private static int generateId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }

}
