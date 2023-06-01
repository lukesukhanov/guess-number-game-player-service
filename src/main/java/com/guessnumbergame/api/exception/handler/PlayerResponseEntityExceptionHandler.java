package com.guessnumbergame.api.exception.handler;

import static java.util.stream.Collectors.toMap;

import java.util.Map;

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

import com.guessnumbergame.api.exception.PlayerNotFoundException;
import com.guessnumbergame.api.exception.PlayerNotUpdatedException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
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

	@ExceptionHandler(PlayerNotFoundException.class)
	protected ResponseEntity<Object> handleNotFoundException(PlayerNotFoundException e, WebRequest request) {
		log.debug("Handling PlayerNotFoundException: {}", e.toString());
		Map<String, Object> responseBody = Map.of("error", e.getMessage());
		HttpHeaders headers = new HttpHeaders();
		return handleExceptionInternal(e, responseBody, headers, HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(PlayerNotUpdatedException.class)
	protected ResponseEntity<Object> handlePlayerNotUpdatedException(PlayerNotUpdatedException e,
			WebRequest request) {
		log.debug("Handling PlayerNotUpdatedException: {}", e.toString());
		Map<String, Object> responseBody = Map.of("error", e.getMessage());
		HttpHeaders headers = new HttpHeaders();
		return handleExceptionInternal(e, responseBody, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

}
