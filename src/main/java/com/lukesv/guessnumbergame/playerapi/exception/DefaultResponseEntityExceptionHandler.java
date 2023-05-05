package com.lukesv.guessnumbergame.playerapi.exception;

import static java.util.stream.Collectors.toMap;

import java.time.Instant;
import java.util.LinkedHashMap;
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

@ControllerAdvice
public class DefaultResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, Object> responseBody = new LinkedHashMap<>(3);
		responseBody.put("status", status.value());
		responseBody.put("timestamp", Instant.now());
		Map<String, String> errors = e.getBindingResult().getFieldErrors()
				.stream()
				.collect(toMap(FieldError::getField, FieldError::getDefaultMessage));
		responseBody.put("errors", errors);
		return new ResponseEntity<>(responseBody, headers, status);
	}

	@ExceptionHandler(PlayerNotFoundException.class)
	protected ResponseEntity<Object> handleNotFoundException(RuntimeException e, WebRequest request) {
		String responseBody = e.getMessage();
		HttpHeaders headers = new HttpHeaders();
		return handleExceptionInternal(e, responseBody, headers, HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(PlayerNotUpdatedException.class)
	protected ResponseEntity<Object> handlePlayerNotUpdatedException(RuntimeException e, WebRequest request) {
		String responseBody = e.getMessage();
		HttpHeaders headers = new HttpHeaders();
		return handleExceptionInternal(e, responseBody, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}