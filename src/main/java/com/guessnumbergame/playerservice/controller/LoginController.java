package com.guessnumbergame.playerservice.controller;

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
 * Provides the endpoint for user authentication.
 * <p>
 * The endpoint {@code /login} is used.
 * <p>
 * The JSON format is used for the response body.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LoginController {

  /**
   * Authenticates the user.
   * <p>
   * Serves the {@code POST} requests for the {@code /login} endpoint.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * POST /login<br />
   * Authorization: Basic vasya:1234
   * <p>
   * The 'username:password' part must be encoded with the Base64 algorithm.
   * <p>
   * <i>Successful authentication</i>
   * <p>
   * Status: 200<br />
   * Body: {username: "vasya"}
   * 
   * @return a {@code ResponseEntity} with the status {@code 200} and the body
   *         containing the authenticated username
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
