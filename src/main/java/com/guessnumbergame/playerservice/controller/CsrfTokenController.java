package com.guessnumbergame.playerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST controller which provides the endpoint for receiving CSRF token.
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
   * Generates a new CSRF token for the authenticated user.
   * <p>
   * Serves the {@code POST} requests for the {@code /login} endpoint.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * POST /csrfToken<br />
   * Cookie: JSESSIONID=[JSESSIONID]
   * <p>
   * <i>Normal response</i>
   * <p>
   * Status: 200<br />
   * X-CSRF-TOKEN: [CSRF token]
   * <p>
   * <i>Response in case authentication has failed</i>
   * <p>
   * Status: 401
   * 
   * @return a {@code ResponseEntity} with the {@code 200} status, the header
   *         {@code X-CSRF-TOKEN} with the new CSRF token and the empty body
   */
  @PostMapping
  ResponseEntity<?> sendCsrfToken(CsrfToken csrfToken) {
    return ResponseEntity
        .ok()
        .header(csrfToken.getHeaderName(), csrfToken.getToken())
        .build();
  }

}
