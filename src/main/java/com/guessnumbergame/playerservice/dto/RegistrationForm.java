package com.guessnumbergame.playerservice.dto;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.guessnumbergame.playerservice.controller.RegistrationController;
import com.guessnumbergame.playerservice.exception.BadRegistrationCredentialsException;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Credentials provided during registration.
 * <p>
 * The {@code equals} method should be used for comparisons.
 * The {@code RegistrationForm} objects are compared by {@code credentials}.
 * <p>
 * This class is immutable and thread-safe.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see User
 * @see RegistrationController
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public final class RegistrationForm implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String credentials;

  public User toUser(PasswordEncoder passwordEncoder) {
    byte[] decodedCredentialsBytes = Base64.getDecoder().decode(this.credentials);
    String[] decodedCredentials = new String(decodedCredentialsBytes, StandardCharsets.UTF_8)
        .split(":");
    if (decodedCredentials.length != 2) {
      throw new BadRegistrationCredentialsException(
          "Credentials must be in format 'username:password'");
    }
    String username = decodedCredentials[0].trim();
    if (username.isEmpty()) {
      throw new BadRegistrationCredentialsException("Username is required");
    }
    String password = decodedCredentials[1];
    if (password.isEmpty()) {
      throw new BadRegistrationCredentialsException("Password is required");
    }
    String hashedPassword = passwordEncoder.encode(password);
    return new User(username, hashedPassword);
  }

  @Override
  public String toString() {
    return "RegistrationForm [credentials=PROTECTED]";
  }

}
