package org.example.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Employee implements Comparable<Employee> {

    @NotNull
    private Integer id;
    private String name;
    private String passportNumber;
    private String education;
    private Address address;
    private String employeeHash;

    public Employee(Integer id, String name, String passportNumber, String education, Address address) {
        this.id = id;
        this.name = name;
        this.passportNumber = passportNumber;
        this.education = education;
        this.address = address;
        this.employeeHash = generateEmployeeHash();
    }

    @Override
    public int compareTo(Employee employee) {
        return this.id.compareTo(employee.id);
    }

    public String generateEmployeeHash() {
        return id + "_" + name;
    }

}
