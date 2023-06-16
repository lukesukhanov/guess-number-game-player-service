package com.guessnumbergame.playerservice.service;

import org.springframework.dao.DuplicateKeyException;

import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.dto.RegistrationForm;
import com.guessnumbergame.playerservice.exception.handler.RegistrationResponseEntityExceptionHandler;

/**
 * The service for a new player registration.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 */
public interface RegistrationService {

  /**
   * Creates a new user and a new player.
   * 
   * @param registrationForm a {@link RegistrationForm} with the new user's
   *        credentials
   * @return the saved {@code PlayerSummary}
   * @throws DuplicateKeyException if the player with this username already
   *         exists
   * @see RegistrationResponseEntityExceptionHandler
   */
  PlayerSummary register(RegistrationForm registrationForm);

}
