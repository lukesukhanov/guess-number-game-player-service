package com.guessnumbergame.playerservice.exception;

import com.guessnumbergame.playerservice.exception.handler.PlayerResponseEntityExceptionHandler;

/**
 * Can be thrown when a player failed to be updated or patched.<br />
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see PlayerResponseEntityExceptionHandler
 */
public class PlayerNotUpdatedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PlayerNotUpdatedException(Long id, RuntimeException cause) {
    super("Failed to update player with id = " + id, cause);
  }

}
