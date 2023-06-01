package com.guessnumbergame.api.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guessnumbergame.api.dto.PlayerSummary;
import com.guessnumbergame.api.dto.RegistrationForm;
import com.guessnumbergame.api.dto.User;
import com.guessnumbergame.api.service.PlayerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

	private final UserDetailsManager userDetailsManager;

	private final PasswordEncoder passwordEncoder;

	private final PlayerService playerService;

	@PostMapping
	@Transactional
	public ResponseEntity<?> register(
			@RequestHeader(HttpHeaders.AUTHORIZATION) RegistrationForm registrationForm) {
		User user = registrationForm.toUser(this.passwordEncoder);
		log.trace("Created a new user: {}", user);
		this.userDetailsManager.createUser(user);
		log.debug("Saved a new user: {}", user);
		PlayerSummary player = new PlayerSummary(null, user.getUsername(), null);
		PlayerSummary savedPlayer = this.playerService.create(player);
		log.debug("Saved a new player: {}", savedPlayer);
		return ResponseEntity.created(URI.create("/players/" + savedPlayer.getId())).body(savedPlayer);
	}

}