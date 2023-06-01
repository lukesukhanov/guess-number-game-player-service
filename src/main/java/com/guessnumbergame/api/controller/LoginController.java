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

@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LoginController {

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
