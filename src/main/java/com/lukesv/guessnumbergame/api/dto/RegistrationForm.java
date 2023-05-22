package com.lukesv.guessnumbergame.api.dto;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
		byte[] decodedPasswordBytes = Base64.getDecoder().decode(this.password);
		String decodedPassword = new String(decodedPasswordBytes, StandardCharsets.UTF_8);
		String hashedPassword = passwordEncoder.encode(decodedPassword);
		return new User(this.username, hashedPassword);
	}
}
