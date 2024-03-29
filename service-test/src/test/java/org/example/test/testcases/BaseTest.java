package org.example.test.testcases;

import static org.example.utils.JsonUtils.readJsonFileAsObject;

import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.clients.MockitoClient;
import org.example.clients.RestClient;
import org.example.clients.WireMockClient;
import org.example.configuration.ContextLoader;
import org.example.exceptions.TestExecutionException;
import org.example.model.Employee;
import org.example.utils.PropertiesReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Slf4j
public class BaseTest extends ContextLoader {
  private static final String DEFAULT_EMPLOYEES_JSON_PATH = "src/test/resources/templates/requests/defaultEmployeesList.json";
  private static final int START_INDEX = 0;

  @Value("${employee.service.host}")
  protected String baseUrl;

  @Autowired
  protected WireMockClient wireMockClient;
  @Autowired
  protected MockitoClient mockitoClient;

  protected static final char PIPE_DELIMITER = '|';
  protected static final char SLASH_DELIMITER = '/';
  protected static final List<Employee> defaultEmployeesList = readJsonFileAsObject(DEFAULT_EMPLOYEES_JSON_PATH, new TypeReference<>() {});

  @BeforeEach
  void setUp() {
    WireMockClient.resetWireMock();
    mockitoClient.resetCaptures();
    mockitoClient.resetMocks();
  }

  @BeforeAll
  public static void preconditions() {
    createDefaultEmployees();
  }

  private static void createDefaultEmployees() {
    Response response = RestClient.getInstance().initializeSpecifications()
        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .sendRequestWithBody(
            Method.PUT,
            PropertiesReader.getInstance().getBaseUrl() + PropertiesReader.getInstance().getAddEmployeeListPath(),
            Collections.emptyMap(),
            defaultEmployeesList);

    if (response.statusCode() != HttpStatus.OK.value()) {
      throw new TestExecutionException("Default Employee list was not created! Cause: %s", response.asPrettyString());
    } else {
      log.info("Default Employees list was created successfully!");
    }
  }

  protected Employee getRandomEmployeeFromDefaultList() {
    return defaultEmployeesList.get(new SecureRandom().nextInt(START_INDEX, defaultEmployeesList.size()));
  }

}
