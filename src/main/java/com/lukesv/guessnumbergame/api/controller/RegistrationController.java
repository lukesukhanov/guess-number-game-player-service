package com.lukesv.guessnumbergame.api.controller;

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

import com.lukesv.guessnumbergame.api.dto.PlayerSummary;
import com.lukesv.guessnumbergame.api.dto.RegistrationForm;
import com.lukesv.guessnumbergame.api.dto.User;
import com.lukesv.guessnumbergame.api.service.PlayerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RegistrationController {

	private final UserDetailsManager userDetailsManager;
	private final PasswordEncoder passwordEncoder;
	private final PlayerService playerService;

	@PostMapping
	@Transactional
	public ResponseEntity<?> register(
			@RequestHeader(HttpHeaders.AUTHORIZATION) RegistrationForm registrationForm) {
		User user = registrationForm.toUser(this.passwordEncoder);
		this.userDetailsManager.createUser(user);
		PlayerSummary player = new PlayerSummary(null, user.getUsername(), null);
		PlayerSummary savedPlayer = this.playerService.create(player);
		return ResponseEntity.created(URI.create("/players/" + savedPlayer.getId())).body(savedPlayer);
	}

}