package org.example.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.client.ExternalClient;
import org.example.client.NoRestClient;
import org.example.clients.MockitoClient;
import org.example.clients.RestClient;
import org.example.clients.WireMockClient;
import org.example.exceptions.TestExecutionException;
import org.example.utils.session.SessionImpl;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import static org.mockito.MockitoAnnotations.openMocks;

@Configuration
@PropertySource(value = "classpath:application-test.properties")
@Import({SessionImpl.class, WireMockClient.class, RestClient.class, MockitoClient.class})
@Slf4j
public class TestConfig {

  @Spy
  private ExternalClient externalClient;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private NoRestClient noRestClient;

  @PostConstruct
  void init() {
    try (AutoCloseable ignored = openMocks(this)) {
      log.info("Mock initialization...");
    } catch (Exception e) {
      throw new TestExecutionException("Error during mock initialization. Cause: %s", e.getMessage());
    }
  }

  @Bean
  @Primary
  ExternalClient getExternalClient() {
    return this.externalClient;
  }

  @Bean
  @Primary
  NoRestClient getNoRestClient() {
    return this.noRestClient;
  }
}
