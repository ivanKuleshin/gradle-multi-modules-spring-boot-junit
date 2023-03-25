package org.example.test.definitionSteps;

import io.cucumber.java.en.Given;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.example.annotations.CucumberComponent;
import org.example.clients.RestClient;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

@CucumberComponent
public class TestDefinitionSteps {

    @Autowired
    RestClient restClient;

    @Given("User performs test")
    public void test1() {
        System.out.println("User performs test");
        Response response = restClient.sendRequestWithoutParams(Method.GET, "/employee");
        System.out.println("Test result: " + response.asPrettyString());

        Assertions.assertFalse(response.asPrettyString().isEmpty());
    }
}
