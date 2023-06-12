package com.guessnumbergame.playerservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.guessnumbergame.playerservice.Application;

@SpringBootTest(classes = Application.class)
@DisplayName("CsrfTokenController")
@Tag("controller")
@Tag("security")
@AutoConfigureMockMvc
class CsrfTokenControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("sendCsrfToken(CsrfToken) - authenticated")
  @WithMockUser
  final void sendCsrfToken_authenticated() throws Exception {
    this.mockMvc.perform(post("/csrfToken"))
        .andExpectAll(
            status().isNoContent(),
            header().exists("X-CSRF-TOKEN"));
  }

  @Test
  @DisplayName("sendCsrfToken(CsrfToken) - unauthenticated")
  final void sendCsrfToken_unauthenticated() throws Exception {
    this.mockMvc.perform(post("/csrfToken"))
        .andExpectAll(
            status().isUnauthorized());
  }

}
