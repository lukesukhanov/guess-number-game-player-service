package com.guessnumbergame.api.exception;

import com.guessnumbergame.api.exception.handler.PlayerResponseEntityExceptionHandler;

/**
 * Can be thrown when a player failed to be updated or patched.<br />
 * 
 * @see PlayerResponseEntityExceptionHandler
 */
public class PlayerNotUpdatedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PlayerNotUpdatedException(Long id) {
    super("Failed to update player with id = " + id);
  }

}
