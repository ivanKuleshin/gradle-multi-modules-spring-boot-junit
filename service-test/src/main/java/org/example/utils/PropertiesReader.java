package org.example.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.example.exceptions.TestExecutionException;

public class PropertiesReader {

  private static final String PROPERTIES_PATH = "src/test/resources/application-test.properties";

  private static final String VALUE_FROM_BRACES_REGEX = "(?<=\\$\\{).+?(?=})";
  private static final String VALUE_WITH_BRACES_REGEX = "\\$\\{(.*?)}";

  private static final Properties PROPERTIES = new Properties();
  private static PropertiesReader propertiesReader;

  private PropertiesReader() {
    this.initializeProperties();
  }

  public static PropertiesReader getInstance() {
    if (propertiesReader == null) {
      propertiesReader = new PropertiesReader();
    }
    return propertiesReader;
  }

  public String getProperty(String propertyName) {
    return resolveProperty(PROPERTIES.getProperty(propertyName));
  }

  public String getBaseUrl() {
      return getProperty("employee.service.host");
  }

  private void initializeProperties() {
    try {
      PROPERTIES.load(new FileReader(PROPERTIES_PATH));
    } catch (IOException e) {
      throw new TestExecutionException("Problem with properties initialization: %s", e.getMessage());
    }
  }

  private String resolveProperty(String propertyValue) {
    Pattern pattern = Pattern.compile(VALUE_FROM_BRACES_REGEX);
    Matcher matcher = pattern.matcher(propertyValue);

    List<String> variableList = new ArrayList<>();
    while (matcher.find()) {
      variableList.add(matcher.group());
    }

    for (String variableName : variableList) {
      propertyValue = propertyValue.replaceFirst(VALUE_WITH_BRACES_REGEX, getProperty(variableName));
    }
    return propertyValue;
  }
}
