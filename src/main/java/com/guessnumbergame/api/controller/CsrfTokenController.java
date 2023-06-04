package com.guessnumbergame.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides the REST endpoint for receiving CSRF token.
 * <p>
 * The JSON format is used for the response body.
 */
@RestController
@RequestMapping("/csrfToken")
public class CsrfTokenController {

  /**
   * Responds with the header "X-CSRF-TOKEN" containing the CSRF token.
   * <p>
   * Processes {@code POST /csrfToken} requests.
   * 
   * @return a {@code ResponseEntity} with status 200, the header "X-CSRF-TOKEN"
   *         and no content
   */
  @PostMapping
  ResponseEntity<?> sendCsrfToken(CsrfToken csrfToken) {
    return ResponseEntity
        .ok()
        .header(csrfToken.getHeaderName(), csrfToken.getToken())
        .build();
  }

}
