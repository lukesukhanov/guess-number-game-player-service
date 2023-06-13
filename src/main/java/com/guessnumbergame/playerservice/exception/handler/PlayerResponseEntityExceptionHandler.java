package com.guessnumbergame.playerservice.exception.handler;

import static java.util.stream.Collectors.toMap;

import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.guessnumbergame.playerservice.controller.PlayerController;
import com.guessnumbergame.playerservice.exception.PlayerNotFoundException;
import com.guessnumbergame.playerservice.exception.PlayerNotUpdatedException;
import com.guessnumbergame.playerservice.service.PlayerService;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles exceptions related to players.
 * <p>
 * This advice works with the {@link PlayerController}.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see PlayerController
 * @see PlayerService
 */
@ControllerAdvice(assignableTypes = PlayerController.class)
@Slf4j
public class PlayerResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    log.debug("Handling MethodArgumentNotValidException: {}", e.toString());
    Map<String, String> errors = e.getBindingResult().getFieldErrors()
        .stream()
        .collect(toMap(FieldError::getField, FieldError::getDefaultMessage));
    Map<String, Object> responseBody = Map.of("errors", errors);
    return new ResponseEntity<>(responseBody, headers, status);
  }

  /**
   * Handles the {@code PlayerNotFoundException} which can be thrown if the
   * player wasn't found in the repository.
   * 
   * @param e the catched {@code PlayerNotFoundException}
   * @param request the current {@code WebRequest}
   * @return a {@code ResponseEntity} after handling the exception
   */
  @ExceptionHandler(PlayerNotFoundException.class)
  public ResponseEntity<Object> handleNotFoundException(PlayerNotFoundException e,
      WebRequest request) {
    log.debug("Handling PlayerNotFoundException: {}", e.toString());
    Map<String, Object> responseBody = Map.of("error", e.getMessage());
    HttpHeaders headers = new HttpHeaders();
    return handleExceptionInternal(e, responseBody, headers, HttpStatus.NOT_FOUND, request);
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
    log.debug("Handling DuplicateKeyException: {}"
        + "cause: {}", e.toString(), e.getCause().toString());
    Map<String, Object> responseBody = Map.of(
        "error", "Duplicating username",
        "cause", e.getCause().getMessage());
    HttpHeaders headers = new HttpHeaders();
    return handleExceptionInternal(e, responseBody, headers, HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Handles the {@code PlayerNotUpdatedException} which can be thrown
   * if the player failed to be updated or patched for technical reasons.
   * 
   * @param e the catched {@code PlayerNotUpdatedException}
   * @param request the current {@code WebRequest}
   * @return a {@code ResponseEntity} after handling the exception
   */
  @ExceptionHandler(PlayerNotUpdatedException.class)
  public ResponseEntity<Object> handlePlayerNotUpdatedException(PlayerNotUpdatedException e,
      WebRequest request) {
    log.warn("Handling PlayerNotUpdatedException: {}\n"
        + "cause: {}", e.toString(), e.getCause().toString());
    Map<String, Object> responseBody = Map.of(
        "error", e.getMessage(),
        "cause", e.getCause().getMessage());
    HttpHeaders headers = new HttpHeaders();
    return handleExceptionInternal(e, responseBody, headers, HttpStatus.INTERNAL_SERVER_ERROR,
        request);
  }

}
