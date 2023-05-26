package com.lukesv.guessnumbergame.api.exception;

public class BadRegistrationCredentials extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BadRegistrationCredentials(String message) {
		super(message);
	}

}
