package org.example.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;


//@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
//@EqualsAndHashCode
public class Employee implements Comparable<Employee> {

    @NotNull
    private Integer id;
    private String name;
    private String passportNumber;
    private String education;
    private Address address;

    public Employee(Integer id, String name, String passportNumber, String education, Address address) {
        this.id = id;
        this.name = name;
        this.passportNumber = passportNumber;
        this.education = education;
        this.address = address;
    }

    public Employee(Integer id, String name, String passportNumber, String education, Address address, String employeeHash) {
        this.id = id;
        this.name = name;
        this.passportNumber = passportNumber;
        this.education = education;
        this.address = address;
        this.employeeHash = employeeHash;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setEmployeeHash(String employeeHash) {
        this.employeeHash = employeeHash;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getEducation() {
        return education;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmployeeHash() {
        return employeeHash;
    }

    private String employeeHash;

    @Override
    public int compareTo(Employee employee) {
        return this.id.compareTo(employee.id);
    }
}
