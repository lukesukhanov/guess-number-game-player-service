package com.lukesv.guessnumbergame.api.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login", produces = "application/json")
public class LoginController {

	@PostMapping
	public ResponseEntity<?> login() {
		Map<String, String> responseBody = Map.of("username",
				SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(responseBody);
	}
}
