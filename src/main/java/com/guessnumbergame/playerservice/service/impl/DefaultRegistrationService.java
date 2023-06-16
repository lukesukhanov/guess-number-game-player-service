package com.guessnumbergame.playerservice.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.dto.RegistrationForm;
import com.guessnumbergame.playerservice.dto.User;
import com.guessnumbergame.playerservice.service.PlayerService;
import com.guessnumbergame.playerservice.service.RegistrationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The default {@code RegistrationService} implementation.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultRegistrationService implements RegistrationService {

  private final UserDetailsManager userDetailsManager;

  private final PasswordEncoder passwordEncoder;

  private final PlayerService playerService;

  @Transactional
  @Override
  public PlayerSummary register(RegistrationForm registrationForm) {
    User user = registrationForm.toUser(this.passwordEncoder);
    log.trace("Created a new User from RegistrationForm: {}", user);
    this.userDetailsManager.createUser(user);
    log.debug("Saved a new user: {}", user);
    PlayerSummary player = new PlayerSummary(null, user.getUsername(), null);
    PlayerSummary savedPlayer = this.playerService.create(player);
    log.debug("Saved a new player: {}", savedPlayer);
    return savedPlayer;
  }

}
