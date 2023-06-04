package com.guessnumbergame.api.exception.handler;

import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.guessnumbergame.api.exception.BadRegistrationCredentialsException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RegistrationResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadRegistrationCredentialsException.class)
  public ResponseEntity<Object> handleBadRegistrationCredentials(
      BadRegistrationCredentialsException e,
      WebRequest request) {
    log.debug("Handling BadRegistrationCredentialsException: {}", e.toString());
    Map<String, Object> responseBody = Map.of("error", e.getMessage());
    HttpHeaders headers = new HttpHeaders();
    return handleExceptionInternal(e, responseBody, headers, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException e,
      WebRequest request) {
    log.debug("Handling DuplicateKeyException: {}", e.toString());
    Map<String, Object> responseBody = Map.of("error", "Duplicate");
    HttpHeaders headers = new HttpHeaders();
    return handleExceptionInternal(e, responseBody, headers, HttpStatus.BAD_REQUEST, request);
  }

}
