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

1. **gradle-multiprojects-test** - This is the parent module.
2. **service** and **service-test** - These are the child modules.
3. Common dependencies are located in the parent `build.gradle` file.
4. Specific dependencies for each module are located in their respective child `build.gradle` files.

### Project Structure

- **Project Name:** `gradle-multiprojects-test`
- **Sub-projects:** `service`, `service-test`

## Files and Configuration

- **settings.gradle:** Specifies `gradle-multiprojects-test` as the root project and lists `service` and `service-test` as its sub-projects.
- **gradlew:** The `gradlew` file is a Gradle Wrapper script. The Gradle Wrapper allows you to execute Gradle builds on machines where Gradle is not installed. It automatically downloads and installs the correct version of Gradle specified for the project.

## Purpose

The purpose of this project is to demonstrate a multi-module project setup using Gradle, integrating Java and Spring Boot for building and testing purposes.
