package org.example.test.definitionSteps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.http.Method;
import org.example.annotations.CucumberComponent;
import org.example.client.ExternalClient;
import org.example.clients.RestClient;
import org.example.clients.WireMockClient;
import org.example.utils.session.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.management.ManagementFactory;

//@Slf4j
@CucumberComponent
public class TagHooks {
  private static final int STATUS_OK = 200;

  @Autowired private WireMockClient wireMockClient;
  @Autowired private RestClient restClient;
  @Autowired protected Session session;
  @Autowired
  ExternalClient externalClient;

  /**
   * We can use these type of annotations for perform some logic before some scenario or some
   * feature
   */
  @Before("@resetWireMock")
  public void resetWireMock() {
    wireMockClient.resetWireMock();
//    log.info("WireMock instance successfully resetted");
  }

  @Before(value = "@testData")
  public void setUp() {
    System.out.println("This method performs only for the Scenarios with @testData");
  }

  /** This method will perform before each feature file */
  @Before
  public void doSetup() {
    // Was added to test the parallel execution:
    long threadId = Thread.currentThread().getId();
    String processName = ManagementFactory.getRuntimeMXBean().getName();
//    log.info(String.format("Started in thread: %s, in JVM: %s", threadId, processName));
  }

  /** This method will perform after each feature file */
  @After
  public void cleanUp() {
    restClient.sendRequestWithoutParams(Method.DELETE, "/employee").then().statusCode(STATUS_OK);
//    log.info("All employees were deleted!");

    session.clear();
//    log.info("Test session was cleaned!");
  }
}
