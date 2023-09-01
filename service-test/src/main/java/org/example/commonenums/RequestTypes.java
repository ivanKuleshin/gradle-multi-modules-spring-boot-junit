package org.example.commonenums;

public enum RequestTypes {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH");
    private final String requestType;

    RequestTypes(String requestType) {
        this.requestType = requestType;
    }

    public String getValue() {
        return requestType;
    }
}
