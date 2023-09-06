package org.example.controller.advice;

import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseMessage handleException(Exception e) {
    return new ResponseMessage(e);
  }

  @ExceptionHandler(ServiceUnavailableException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public ResponseMessage handleServiceUnavailableException(Exception e) {
    return new ResponseMessage(e);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseMessage handleBadRequestException(Exception e) {
    return new ResponseMessage(e);
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseMessage handleNotFoundException(Exception e) {
    return new ResponseMessage(e);
  }
}
