package com.guessnumbergame.playerservice.exception;

import com.guessnumbergame.playerservice.dto.RegistrationForm;
import com.guessnumbergame.playerservice.exception.handler.RegistrationResponseEntityExceptionHandler;

/**
 * Can be thrown by {@code RegistrationForm#toUser} during credentials
 * pre-validation.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see RegistrationForm#toUser(org.springframework.security.crypto.password.PasswordEncoder)
 * @see RegistrationResponseEntityExceptionHandler
 */
public class BadRegistrationCredentialsException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public BadRegistrationCredentialsException(String message) {
    super(message);
  }

}
