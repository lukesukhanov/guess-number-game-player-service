package com.guessnumbergame.playerservice.exception;

import com.guessnumbergame.playerservice.exception.handler.PlayerResponseEntityExceptionHandler;

/**
 * Can be thrown if a player wasn't found.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see PlayerResponseEntityExceptionHandler
 */
public class PlayerNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PlayerNotFoundException(Long id) {
    super("Can't find player with id = " + id);
  }

  public PlayerNotFoundException(String username) {
    super("Can't find player with username '%s'".formatted(username));
  }

  public PlayerNotFoundException() {
    super("Can't find any player");
  }

}
