package com.lukesv.guessnumbergame.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csrfToken")
public class CsrfTokenController {

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	ResponseEntity<?> sendCsrfToken(CsrfToken csrfToken) {
		return ResponseEntity.ok().header(csrfToken.getHeaderName(), csrfToken.getToken()).build();
	}
}
