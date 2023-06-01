package com.guessnumbergame.api.dto;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.guessnumbergame.api.exception.BadRegistrationCredentials;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RegistrationForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String credentials;

	public User toUser(PasswordEncoder passwordEncoder) {
		byte[] decodedCredentialsBytes = Base64.getDecoder().decode(this.credentials);
		String[] decodedCredentials = new String(decodedCredentialsBytes, StandardCharsets.UTF_8).split(":");
		if (decodedCredentials.length != 2) {
			throw new BadRegistrationCredentials("Credentials must be in format 'username:password'");
		}
		String username = decodedCredentials[0].trim();
		if (username.isEmpty()) {
			throw new BadRegistrationCredentials("Username is required");
		}
		String password = decodedCredentials[1];
		if (password.isEmpty()) {
			throw new BadRegistrationCredentials("Password is required");
		}
		String hashedPassword = passwordEncoder.encode(password);
		return new User(username, hashedPassword);
	}

	@Override
	public String toString() {
		return "RegistrationForm [credentials=PROTECTED]";
	}

}
