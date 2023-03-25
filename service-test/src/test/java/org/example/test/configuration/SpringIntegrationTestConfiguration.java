package org.example.test.configuration;

/**
 * The @SpringBootTest annotation tells Spring Boot to look for a main configuration class (one with @SpringBootApplication, for instance)
 * and use that to start a Spring application context
 * <p>
 * RestTemplate already knows about host and port for integration tests
 * <p>
 * To make Cucumber aware of your test configuration you can annotate a configuration class with @CucumberContextConfiguration
 * <p>
 * The @ContextConfiguration(loader = SpringBootContextLoader.class) - loads an ApplicationContext for Spring integration test.
 * <p>
 * The  @TestConfiguration - We use @TestConfiguration to modify Spring’s application context during test runtime.
 * We can use it to override certain bean definitions, for example to replace real beans with fake beans or
 * to change the configuration of a bean to make it better testable.
 **/
//@ActiveProfiles(value = "test")
//@CucumberContextConfiguration
//@SpringBootTest(classes = EmployeeRestServiceNoDbApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWireMock(port = 0)
////@PropertySource("classpath:application-test.yml")
//@Import({SessionImpl.class, WireMockClient.class, RestClient.class})
//public class SpringIntegrationTestConfiguration {
//
//    public static String baseUrl;
//
//    @LocalServerPort
//    private int port;
//    @Value("${employee.service.host}")
//    private String employeeServiceHost;
//
//    @PostConstruct
//    private void init() {
//        baseUrl = String.format("http://%s:%s", employeeServiceHost, port);
//    }
//}
