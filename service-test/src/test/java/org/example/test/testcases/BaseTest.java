package org.example.test.testcases;

import org.example.clients.WireMockClient;
import org.example.configuration.ContextLoader;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest extends ContextLoader {

  @BeforeEach
  void setUp() {
    WireMockClient.resetWireMock();
  }

}
