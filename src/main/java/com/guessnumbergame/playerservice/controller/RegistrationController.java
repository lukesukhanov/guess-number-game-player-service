package com.guessnumbergame.playerservice.controller;

import java.net.URI;

import org.springframework.dao.DuplicateKeyException;
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

import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.dto.RegistrationForm;
import com.guessnumbergame.playerservice.dto.User;
import com.guessnumbergame.playerservice.exception.handler.RegistrationResponseEntityExceptionHandler;
import com.guessnumbergame.playerservice.service.PlayerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The REST controller which provides the endpoint for a new player
 * registration.
 * <p>
 * The endpoint {@code /register} is used.
 * <p>
 * The JSON format is used for the response body.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see RegistrationForm
 */
@RestController
@RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

  private final UserDetailsManager userDetailsManager;

  private final PasswordEncoder passwordEncoder;

  private final PlayerService playerService;

  /**
   * Creates a new user and a new player.
   * <p>
   * Serves the {@code POST} requests for the {@code /register} endpoint.
   * <h2>Usage examples</h2>
   * <b>Request</b>
   * <p>
   * POST /register<br />
   * Authorization: [username]:[password]<br />
   * <p>
   * The '[username]:[password]' part must be encoded with Base64.
   * <p>
   * <b>Normal response</b>
   * <p>
   * Status: 201<br />
   * Location: [/players/[id]]<br />
   * Body: { "id": "[id]", "username": "[username]", "bestAttemptsCount":
   * "null"}
   * <p>
   * <b>Response in case the user with the given username already exists</b>
   * <p>
   * Status: 400<br />
   * Body: {"error": "The user with this username already exists"}
   * 
   * @param registrationForm a {@link RegistrationForm} with the new user's
   *        credentials
   * @return a {@code ResponseEntity} with the {@code 201} status and the body
   *         containing
   *         {@link PlayerSummary} with information about the new player
   * @throws DuplicateKeyException if the player with the given username already
   *         exists
   * @see RegistrationResponseEntityExceptionHandler
   */
  @PostMapping
  @Transactional
  public ResponseEntity<PlayerSummary> register(
      @RequestHeader(HttpHeaders.AUTHORIZATION) RegistrationForm registrationForm) {
    User user = registrationForm.toUser(this.passwordEncoder);
    log.trace("Created a new User from RegistrationForm: {}", user);
    this.userDetailsManager.createUser(user);
    log.debug("Saved a new user: {}", user);
    PlayerSummary player = new PlayerSummary(null, user.getUsername(), null);
    PlayerSummary savedPlayer = this.playerService.create(player);
    log.debug("Saved a new player: {}", savedPlayer);
    return ResponseEntity
        .created(URI.create("/players/" + savedPlayer.getId()))
        .body(savedPlayer);
  }

}
