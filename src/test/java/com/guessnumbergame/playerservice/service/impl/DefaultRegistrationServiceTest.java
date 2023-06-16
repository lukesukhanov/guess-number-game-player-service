package com.guessnumbergame.playerservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import com.guessnumbergame.playerservice.Application;
import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.dto.RegistrationForm;
import com.guessnumbergame.playerservice.dto.User;
import com.guessnumbergame.playerservice.service.PlayerService;

@SpringBootTest(classes = Application.class)
@DisplayName("DefaultRegistrationService")
@Tag("service")
@Tag("security")
class DefaultRegistrationServiceTest {

  @MockBean
  private UserDetailsManager userDetailsManager;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private PlayerService playerService;

  @Autowired
  DefaultRegistrationService registrationService;

  @Test
  @DisplayName("register(RegistrationForm) - normal return")
  final void register_normalReturn() {
    String credentials = "username:password";
    byte[] credentialsBytes = credentials.getBytes(StandardCharsets.UTF_8);
    String encodedCredentials = Base64.getEncoder().encodeToString(credentialsBytes);
    RegistrationForm registrationForm = new RegistrationForm(encodedCredentials);
    PlayerSummary player = new PlayerSummary(null, "username", null);
    PlayerSummary savedPlayer = new PlayerSummary(1L, "username", null);
    when(this.passwordEncoder.encode("password"))
        .thenReturn("password");
    when(this.playerService.create(player))
        .thenReturn(savedPlayer);
    assertEquals(this.registrationService.register(registrationForm), savedPlayer);
  }

  @Test
  @DisplayName("register(RegistrationForm) - duplicating username")
  final void register_duplicatingUsername() throws Exception {
    String credentials = "username:password";
    byte[] credentialsBytes = credentials.getBytes(StandardCharsets.UTF_8);
    String encodedCredentials = Base64.getEncoder().encodeToString(credentialsBytes);
    RegistrationForm registrationForm = new RegistrationForm(encodedCredentials);
    User user = new User("username", "password");
    when(this.passwordEncoder.encode("password"))
        .thenReturn("password");
    doThrow(DuplicateKeyException.class)
        .when(this.userDetailsManager).createUser(user);
    assertThrows(DuplicateKeyException.class,
        () -> this.registrationService.register(registrationForm));
  }

}
