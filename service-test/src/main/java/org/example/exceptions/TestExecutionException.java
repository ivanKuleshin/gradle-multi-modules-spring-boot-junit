package org.example.exceptions;

public class TestExecutionException extends RuntimeException {

    public TestExecutionException(String message, Object ... parameter) {
        super(String.format(message, parameter));
    }

    public TestExecutionException(String message) {
        super(message);
    }

}
