# Project Overview
This is a project using Spring Boot Test and Gradle.

## Technologies Used

- **JUnit 5**
    - JUnit Jupiter is the next generation of JUnit testing framework for Java, designed for writing and running tests.

- **Rest Assured**
    - Rest Assured is a Java library for testing and validating REST services, providing a fluent API to make HTTP requests and assert responses.

- **Spring Boot**
    - Spring Boot simplifies the setup and development of Spring-based applications by providing default configurations and dependencies.

- **Spring Cloud Contract WireMock**
    - Spring Cloud Contract WireMock integrates WireMock, a simulator for HTTP-based APIs, with Spring Cloud Contract.

- **WireMock**
    - WireMock is a simulator for HTTP-based APIs, useful for stubbing and mocking HTTP services in tests.

- **Apache Commons Collections**
    - Apache Commons Collections provides additional collection implementations and utilities for Java.

- **Lombok**
    - Description: Lombok is a library that helps reduce boilerplate code in Java classes by providing annotations to generate methods such as getters, setters, and constructors.

## Overview

1. `gradle-multiprojects-test` - This is the parent module.
2. `service` and `service-test` - These are the child modules.
3. Common dependencies are located in the parent `build.gradle` file.
4. Specific dependencies for each module are located in their respective child `build.gradle` files.

## Test Suites

The project uses JUnit 5 for testing and has defined two test suites: `MasterSuite` and `SequentialSuite`.

- **MasterSuite**: This is the main test suite that includes all the tests to be run. It includes two other suites: `SequentialSuite` and `ParallelSuite`.
- **SequentialSuite**: This suite includes all tests in the `org.example.test.testcases` package that are tagged 
  with "sequence" label. These tests are run sequentially.
- **ParallelSuite**: This suite includes all tests in the `org.example.test.testcases` package that are tagged with 
  "parallel" label. These tests are run in parallel.

## Running Tests

To run the tests, you can use the custom Gradle task `compTests`. This task is configured to run the `MasterSuite` test suite and enables parallel execution of tests. It also includes an event listener for the `afterTest` event, which logs a warning message if any component tests fail.

You can run this task from the command line with the following command:

```bash
./gradlew compTests
```

## Files and Configuration

- **settings.gradle:** Specifies `gradle-multiprojects-test` as the root project and lists `service` and `service-test` as its sub-projects.
- **gradlew:** The `gradlew` file is a Gradle Wrapper script. The Gradle Wrapper allows you to execute Gradle builds on machines where Gradle is not installed. It automatically downloads and installs the correct version of Gradle specified for the project.

## Purpose

The purpose of this project is to demonstrate a multi-module project setup using Gradle, integrating Java and Spring Boot for building and testing purposes.
