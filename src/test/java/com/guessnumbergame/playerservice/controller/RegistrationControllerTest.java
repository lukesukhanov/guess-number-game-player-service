package com.guessnumbergame.playerservice.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guessnumbergame.playerservice.Application;
import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.dto.User;
import com.guessnumbergame.playerservice.service.PlayerService;

@SpringBootTest(classes = Application.class)
@DisplayName("RegistrationController")
@Tag("controller")
@Tag("security")
@AutoConfigureMockMvc
class RegistrationControllerTest {

  @MockBean
  private UserDetailsManager userDetailsManager;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private PlayerService playerService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("register(RegistrationForm) - normal return")
  final void register_normalReturn() throws Exception {
    String credentials = "username:password";
    byte[] credentialsBytes = credentials.getBytes(StandardCharsets.UTF_8);
    String encodedCredentials = Base64.getEncoder().encodeToString(credentialsBytes);
    PlayerSummary player = new PlayerSummary(null, "username", null);
    PlayerSummary savedPlayer = new PlayerSummary(1L, "username", null);
    when(this.passwordEncoder.encode("password"))
        .thenReturn("password");
    when(this.playerService.create(player))
        .thenReturn(savedPlayer);
    this.mockMvc.perform(post("/register")
        .accept(MediaType.APPLICATION_JSON)
        .header("Registration", encodedCredentials))
        .andExpectAll(
            status().isCreated(),
            header().string(HttpHeaders.LOCATION, "/players/1"),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(objectMapper.writeValueAsString(savedPlayer)));
  }

  @Test
  @DisplayName("register(RegistrationForm) - duplicating username")
  final void register_duplicatingUsername() throws Exception {
    String credentials = "username:password";
    byte[] credentialsBytes = credentials.getBytes(StandardCharsets.UTF_8);
    String encodedCredentials = Base64.getEncoder().encodeToString(credentialsBytes);
    User user = new User("username", "password");
    when(this.passwordEncoder.encode("password"))
        .thenReturn("password");
    doThrow(DuplicateKeyException.class)
        .when(this.userDetailsManager).createUser(user);
    this.mockMvc.perform(post("/register")
        .accept(MediaType.APPLICATION_JSON)
        .header("Registration", encodedCredentials))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(objectMapper
                .writeValueAsString(
                    Map.of("error", "Duplicating username"))));
  }

}
