package com.lukesv.guessnumbergame.playerapi.exception;

public class PlayerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PlayerNotFoundException(Long id) {
		super("Can't find a player with id = " + id);
	}

}
