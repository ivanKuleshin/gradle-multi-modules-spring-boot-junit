package org.example.test.configuration;

import io.cucumber.spring.CucumberContextConfiguration;
import org.example.EmployeeRestServiceNoDbApplication;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles(value = "test")
@SpringBootTest(classes = EmployeeRestServiceNoDbApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = TestConfig.class, loader = SpringBootContextLoader.class)
@CucumberContextConfiguration
@AutoConfigureWireMock()
public class ContextLoader {

}
