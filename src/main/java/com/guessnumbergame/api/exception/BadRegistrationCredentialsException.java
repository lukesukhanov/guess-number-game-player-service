package com.guessnumbergame.api.exception;

import com.guessnumbergame.api.dto.RegistrationForm;
import com.guessnumbergame.api.exception.handler.RegistrationResponseEntityExceptionHandler;

/**
 * Can be thrown by {@code RegistrationForm#toUser} during credentials
 * pre-validation.
 * 
 * @see RegistrationForm#toUser(org.springframework.security.crypto.password.PasswordEncoder)
 * @see RegistrationResponseEntityExceptionHandler
 */
public class BadRegistrationCredentialsException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public BadRegistrationCredentialsException(String message) {
    super(message);
  }

}
