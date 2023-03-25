package org.example.controller.advice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@Setter
@ToString
class ResponseMessage {

    private String message;
    private String stackTrace;

    ResponseMessage(Exception e) {
        this.message = e.getMessage();
        this.stackTrace = Arrays.toString(e.getStackTrace());
    }
}
