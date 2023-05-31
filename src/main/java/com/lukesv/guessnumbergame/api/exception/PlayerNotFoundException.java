package com.lukesv.guessnumbergame.api.exception;

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
