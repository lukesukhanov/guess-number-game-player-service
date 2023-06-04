package com.guessnumbergame.api.exception;

import com.guessnumbergame.api.exception.handler.PlayerResponseEntityExceptionHandler;

/**
 * Can be thrown if a player wasn't found.
 * 
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
