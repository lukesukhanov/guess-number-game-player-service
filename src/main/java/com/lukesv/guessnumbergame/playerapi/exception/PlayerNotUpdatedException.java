package com.lukesv.guessnumbergame.playerapi.exception;

public class PlayerNotUpdatedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PlayerNotUpdatedException(Long id) {
		super("Failed to update a player with id = " + id);
	}
	
}
