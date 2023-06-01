package com.guessnumbergame.api.exception;

public class PlayerNotUpdatedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PlayerNotUpdatedException(Long id) {
		super("Failed to update player with id = " + id);
	}

}
