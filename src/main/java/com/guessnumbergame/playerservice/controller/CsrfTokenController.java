package com.guessnumbergame.playerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides the endpoint for receiving a new CSRF token.
 * <p>
 * The endpoint {@code /csrfToken} is used.
 * <p>
 * The JSON format is used for the response body.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 */
@RestController
@RequestMapping("/csrfToken")
public class CsrfTokenController {

  /**
   * Generates a new CSRF token. Requires authentication.
   * <p>
   * Serves the {@code POST} requests for the {@code /login} endpoint.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * POST /csrfToken<br />
   * <p>
   * <i>A new CSRF token was created</i>
   * <p>
   * Status: 204<br />
   * X-CSRF-TOKEN: [CSRF token]
   * 
   * @return a {@code ResponseEntity} with the status {@code 204} and the header
   *         {@code X-CSRF-TOKEN} containing a new CSRF token
   */
  @PostMapping
  ResponseEntity<?> getCsrfToken(CsrfToken csrfToken) {
    return ResponseEntity
        .noContent()
        .header(csrfToken.getHeaderName(), csrfToken.getToken())
        .build();
  }

}
