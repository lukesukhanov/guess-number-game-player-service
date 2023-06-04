package com.guessnumbergame.api.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Provides the REST endpoint for user authentication.
 * <p>
 * The JSON format is used for the response body.
 */
@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LoginController {

  /**
   * Responds with the authenticated user's username in format
   * {@code {"username": "username"}}}
   * <p>
   * Processes {@code POST /login} requests.
   * 
   * @return a {@code ResponseEntity} with status 200 and the body containing
   *         the authenticated user's username or with status 204 and no content
   *         in case of failed authentication
   */
  @PostMapping
  public ResponseEntity<?> login() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    log.trace("Loaded authentication: " + authentication);
    if (authentication == null) {
      log.debug("Failed to find Authentication in current SecurityContext");
      return ResponseEntity.noContent().build();
    }
    String username = authentication.getName();
    Map<String, String> responseBody = Map.of("username", username);
    return ResponseEntity.ok(responseBody);
  }

}
