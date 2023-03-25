package org.example.test.definitionSteps;

import io.cucumber.java.ParameterType;
import io.restassured.http.Method;
import org.example.clients.RestClient;
import org.example.model.Employee;

import java.util.List;
import java.util.stream.Stream;

public class TypeRegistryConfiguration {

  @ParameterType(value = ".*")
  public RestClient.RequestTypes requestType(String requestType) {
    return RestClient.RequestTypes.valueOf(requestType);
  }

  @ParameterType(value = ".*")
  public Employee singleEmployee(String employeeParams) {
    List<String> params =
            Stream.of(employeeParams.split(",")).map(String::trim).toList();

    return new Employee(
        Integer.valueOf(params.get(0)), params.get(1), params.get(2), params.get(3), null, null);
  }

  @ParameterType("[A-Z]+")
  public Method httpMethod(String requestType) {
    return Method.valueOf(requestType);
  }

//  @DataTableType
//  public Employee employeeEntry(Map<String, String> row) {
//    return Employee.builder()
//        .id(Integer.valueOf(row.get("id")))
//        .name(row.get("name"))
//        .passportNumber(row.get("passportNumber"))
//        .education(row.get("education"))
//        .address(
//            Address.builder()
//                .city(row.get("address.city"))
//                .country(row.get("address.country"))
//                .zip(row.get("address.zip"))
//                .build())
//        .employeeHash(row.get("employeeHash"))
//        .build();
//  }
//
//  @DataTableType
//  public Address addressEntry(Map<String, String> row) {
//    return Address.builder()
//        .city(row.get("city"))
//        .country(row.get("country"))
//        .zip(row.get("zip"))
//        .build();
//  }
}
