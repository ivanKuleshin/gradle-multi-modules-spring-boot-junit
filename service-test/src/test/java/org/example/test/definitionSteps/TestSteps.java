package org.example.test.definitionSteps;

import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@Slf4j
public class TestSteps {

    @Value("${ivan.test.name}")
    private String myName;

    @Given("user retrieves data from properties and log it")
    public void testStep() {
//        log.info("My name is " + myName);

        assertThat(myName).isEqualTo("Ivan");
    }
}
