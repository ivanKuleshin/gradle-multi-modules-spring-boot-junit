package org.example.test.definitionSteps;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.example.clients.RestClient;
import org.example.controller.EmployeeController;
import org.example.exception.CustomRuntimeException;
import org.example.exceptions.TestExecutionException;
import org.example.model.Employee;
import org.example.utils.session.Session;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.utils.TestUtil.castMapToObject;
import static org.example.utils.TestUtil.convertValueToList;
import static org.example.utils.session.SessionKey.ACTUAL_LIST;
import static org.example.utils.session.SessionKey.ACTUAL_RESULT;
import static org.example.utils.session.SessionKey.EXPECTED_LIST;
import static org.example.utils.session.SessionKey.EXPECTED_RESULT;
import static org.example.utils.session.SessionKey.RESPONSE;

public class EmployeeSteps {

    @Autowired
    private RestClient restClient;
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected Session session;
    @Autowired
    protected EmployeeController controller;

    @Value("${employee.service.host}")
    private String baseUrl;

    private static final int FIRST = 0;
    private static final int STATUS_OK = 200;
    private static final String ALL_VALUES = "$";

    static final TypeReference<List<Employee>> employeeListTypeReference = new TypeReference<>() {
    };

    @Given("Employee '{singleEmployee}' added to Employee rest service repository")
    public void addEmployee(Employee employee) {
        restTemplate.put("/employee", employee);

        ResponseEntity<Employee> forEntity = restTemplate.getForEntity("/employee/" + employee.getId(), Employee.class);

        Assert.assertTrue(forEntity.getStatusCode().is2xxSuccessful());
    }

    @Given("employees added to Employee rest service repository:")
    public void addListOfEmployees(List<Employee> employees) {
        Response response = restClient.sendRequestWithBody(Method.PUT, "/employee/list", employees);

        if (response.statusCode() != STATUS_OK) {
            throw new TestExecutionException("Test data was NOT added to service repository");
        }

        session.put(EXPECTED_RESULT, employees);
    }

    @When("we send {string} request to the {string} endpoint")
    public void weSendGETRequestToTheEmployeeEndpoint(String methodName, String endpoint) {
        ResponseEntity<List<Employee>> responseEntity = restTemplate.exchange(
                String.format("%s/%s", baseUrl, endpoint),
                HttpMethod.valueOf(methodName),
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        List<Employee> actualList = Objects.requireNonNull(responseEntity.getBody()).stream().sorted().collect(Collectors.toList());
        session.put(ACTUAL_LIST, actualList);
    }

    @When("the {httpMethod} request is sent to the {string} endpoint with params:")
    public void theRequestIsSentToTheEmployeeEndpointWithParams(Method requestType, String endpoint, Map<String, String> params) {
        var response = restClient.sendRequestWithParams(requestType, endpoint, params);
        session.put(RESPONSE, response);
    }

    @When("the {httpMethod} request is sent to the {string} endpoint without params")
    public void theRequestIsSentToTheEmployeeEndpointWithoutParams(Method requestType, String endpoint) {
        var response = restClient.sendRequestWithoutParams(requestType, endpoint);
        session.put(RESPONSE, response);
    }

    @When("the {httpMethod} request is sent to the {string} endpoint with body")
    public void theRequestIsSentToTheEmployeeEndpointWithBody(Method requestType, String endpoint, List<Map<String, String>> requestBody) {
        Response response;

        if (requestBody.size() == 1) {
            response = restClient.sendRequestWithBody(requestType, endpoint, requestBody.get(FIRST));
        } else {
            response = restClient.sendRequestWithBody(requestType, endpoint, requestBody);
        }

        session.put(RESPONSE, response);
    }

    @Then("the status code is {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        Response response = session.get(RESPONSE, Response.class);
        int actualStatusCode = response.getStatusCode();

        assertThat(actualStatusCode)
                .as("Status code mismatch! \n Response is %s", response.getBody().print())
                .isEqualTo(expectedStatusCode);
    }

    @And("error message contains: {string}")
    public void verifyErrorMessage(String expectedMessage) {
        Response response = session.get(RESPONSE, Response.class);
        String actualMessage = response.getBody().jsonPath().getMap(ALL_VALUES).get("message").toString();

        assertThat(actualMessage).as("Error message mismatch!").contains(expectedMessage);
    }

    @Then("retrieved data is equal to added data")
    public void verifyRetrievedDataIsEqualToAddedData() {
        var responseBody = session.get(RESPONSE, Response.class).jsonPath().get(ALL_VALUES);

        List<Employee> actualResult = convertValueToList(responseBody, employeeListTypeReference);
        List<Employee> expectedResult = convertValueToList(session.get(EXPECTED_RESULT), employeeListTypeReference);
        expectedResult = actualResult.stream().filter(expectedResult::contains).collect(Collectors.toList());

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Then("employee with {int} ID has been added to the repository correctly")
    public void verifyEmployeeHasBeenAdded(int employeeId){
        Response response = restClient.sendRequestWithParams(Method.GET, "/employee/{id}", Map.of("id", employeeId))
                .then().statusCode(STATUS_OK).extract().response();
        Employee actualEmployee = castMapToObject(response.jsonPath().get(ALL_VALUES), Employee.class);

        Employee expectedEmployee = castMapToObject(session.get(EXPECTED_RESULT), Employee.class);

        assertThat(actualEmployee).isEqualTo(expectedEmployee);
    }

    @Then("employee with id {int} is deleted from the repository")
    public void verifyEmployeeIsDeleted(int employeeId) {
        Response response = restClient.sendRequestWithoutParams(Method.GET, "/employee")
                .then().statusCode(STATUS_OK).extract().response();

        List<Employee> actualResponseList = convertValueToList(response.jsonPath().get(ALL_VALUES),
                employeeListTypeReference);

        assertThat(actualResponseList.stream().anyMatch(employee -> employee.getId() == employeeId))
                .as("Employee with id %s was NOT deleted!", employeeId)
                .isFalse();
    }

    @When("we send {string} request to the {string} endpoint with {int} id")
    public void weSendGETRequestToTheEmployeeEndpointWithId(String methodName, String endpoint, int id) {
        ResponseEntity<Employee> responseEntity = restTemplate.exchange(
                String.format("%s/%s/%s", baseUrl, endpoint, id),
                HttpMethod.valueOf(methodName),
                null,
                Employee.class
        );

        Employee actual = Objects.requireNonNull(responseEntity.getBody());
        session.put(ACTUAL_RESULT, actual);

        Employee expected = convertValueToList(session.get(EXPECTED_LIST), employeeListTypeReference)
                .stream().filter(employee -> employee.getId() == id).findFirst()
                .orElseThrow(() -> new CustomRuntimeException(String.format("Expected employee not found with id = %s", id)));

        session.put(EXPECTED_RESULT, expected);
    }

    @Then("retrieved data is equal to added data for specified id")
    public void retrievedDataIsEqualToAddedDataForSpecifiedId() {
        assertThat(session.get(ACTUAL_RESULT, Employee.class))
                .isEqualTo(session.get(EXPECTED_RESULT, Employee.class));
    }

    @Given("User sees that our controller is not NULL")
    public void controllerIsNotNull() {
        assertThat(controller).isNotNull();
//        log.info("Controller checking...");
    }
}
