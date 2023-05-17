package com.lukesv.guessnumbergame.api.dto;

import java.io.Serializable;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class RegistrationForm implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private final String username;
	private final String password;

	public User toUserEntity(PasswordEncoder passwordEncoder) {
		return new User(username, passwordEncoder.encode(password));
	}
}
