package com.lukesv.guessnumbergame.api.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

	@PostMapping
	public ResponseEntity<?> login() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Map<String, String> responseBody = Map.of("username", username);
		return ResponseEntity.ok(responseBody);
	}
}
