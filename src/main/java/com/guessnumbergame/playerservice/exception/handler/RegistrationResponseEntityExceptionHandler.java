package com.guessnumbergame.playerservice.exception.handler;

import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.guessnumbergame.playerservice.controller.RegistrationController;
import com.guessnumbergame.playerservice.dto.RegistrationForm;
import com.guessnumbergame.playerservice.exception.BadRegistrationCredentialsException;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles exceptions related to registration process.
 * <p>
 * This advice works with the {@link RegistrationController}.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see RegistrationController
 * @see RegistrationForm
 */
@ControllerAdvice(assignableTypes = RegistrationController.class)
@Slf4j
public class RegistrationResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles the {@code BadRegistrationCredentialsException} which can be thrown
   * while decoding the received credentials.
   * 
   * @param e the catched {@code BadRegistrationCredentialsException}
   * @param request the current {@code WebRequest}
   * @return a {@code ResponseEntity} after handling the exception
   * @see RegistrationForm#toUser(PasswordEncoder)
   */
  @ExceptionHandler(BadRegistrationCredentialsException.class)
  public ResponseEntity<Object> handleBadRegistrationCredentials(
      BadRegistrationCredentialsException e, WebRequest request) {
    log.debug("Handling BadRegistrationCredentialsException: {}", e.toString());
    Map<String, Object> responseBody = Map.of("error", e.getMessage());
    HttpHeaders headers = new HttpHeaders();
    return handleExceptionInternal(e, responseBody, headers, HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Handles the {@code DuplicateKeyException} which can be thrown during
   * registration process if the given username is already in use.
   * 
   * @param e the catched {@code DuplicateKeyException}
   * @param request the current {@code WebRequest}
   * @return a {@code ResponseEntity} after handling the exception
   */
  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException e,
      WebRequest request) {
    log.debug("Handling DuplicateKeyException: {}", e.toString());
    Map<String, Object> responseBody = e.getMessage().contains("Key (username)")
        ? Map.of("error", "Duplicating username")
        : Map.of("error", e.toString());
    HttpHeaders headers = new HttpHeaders();
    return handleExceptionInternal(e, responseBody, headers, HttpStatus.BAD_REQUEST, request);
  }

}
