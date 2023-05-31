package com.lukesv.guessnumbergame.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csrfToken")
public class CsrfTokenController {

	@PostMapping
	ResponseEntity<?> sendCsrfToken(CsrfToken csrfToken) {
		return ResponseEntity.ok().header(csrfToken.getHeaderName(), csrfToken.getToken()).build();
	}

}
