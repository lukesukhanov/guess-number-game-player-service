package com.guessnumbergame.playerservice.controller;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guessnumbergame.playerservice.Application;
import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.exception.PlayerNotFoundException;
import com.guessnumbergame.playerservice.service.PlayerService;

@SpringBootTest(classes = Application.class)
@DisplayName("PlayerController")
@Tag("controller")
@Tag("player")
@AutoConfigureMockMvc
class PlayerControllerTest {

  @MockBean
  private PlayerService playerService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private final List<PlayerSummary> existingPlayers;

  {
    List<PlayerSummary> players = new ArrayList<>(3);
    players.add(new PlayerSummary(1l, "username1", 1));
    players.add(new PlayerSummary(2l, "username2", 2));
    players.add(new PlayerSummary(3l, "username3", 3));
    this.existingPlayers = Collections.unmodifiableList(players);
  }

  @Test
  @DisplayName("getAll() - normal return")
  final void getAll_normalReturn() throws Exception {
    when(this.playerService.getAll())
        .thenReturn(this.existingPlayers);
    this.mockMvc.perform(get("/players")
        .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(this.objectMapper.writeValueAsString(this.playerService.getAll())));
  }

  @Test
  @DisplayName("getAll() - empty return")
  final void getAll_emptyReturn() throws Exception {
    when(this.playerService.getAll())
        .thenReturn(Collections.emptyList());
    this.mockMvc.perform(get("/players")
        .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(this.objectMapper.writeValueAsString(Collections.emptyList())));
  }

  @Test
  @DisplayName("getById(Long) - normal return")
  final void getById_normalReturn() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(id, "username", 1);
    when(this.playerService.getById(id))
        .thenReturn(player);
    this.mockMvc.perform(get("/players/" + id)
        .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(this.objectMapper.writeValueAsString(player)));
  }

  @Test
  @DisplayName("getById(Long) - player not found")
  final void getById_playerNotFound() throws Exception {
    Long id = 1L;
    PlayerNotFoundException e = new PlayerNotFoundException(id);
    when(this.playerService.getById(id))
        .thenThrow(e);
    this.mockMvc.perform(get("/players/" + id)
        .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(this.objectMapper.writeValueAsString(Map.of("error", e.getMessage()))));
  }

  @Test
  @DisplayName("getByUsername(String) - normal return")
  final void getByUsername_normalReturn() throws Exception {
    String username = "username";
    PlayerSummary player = new PlayerSummary(1L, username, 1);
    when(this.playerService.getByUsername(username))
        .thenReturn(player);
    this.mockMvc.perform(get("/players?username=" + username)
        .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(this.objectMapper.writeValueAsString(player)));
  }

  @Test
  @DisplayName("getByUsername(String) - player not found")
  final void getByUsername_playerNotFound() throws Exception {
    String username = "username";
    PlayerNotFoundException e = new PlayerNotFoundException(username);
    when(this.playerService.getByUsername(username))
        .thenThrow(e);
    this.mockMvc.perform(get("/players?username=" + username)
        .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().json(this.objectMapper.writeValueAsString(Map.of("error", e.getMessage()))));
  }

  @Test
  @DisplayName("getPlayersWithBestResult() - normal return")
  final void getPlayerWithBestResult_normalReturn() throws Exception {
    when(this.playerService.getPlayersWithBestResult())
        .thenReturn(this.existingPlayers);
    this.mockMvc.perform(get("/players/withBestResult")
        .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(this.objectMapper.writeValueAsString(this.existingPlayers)));
  }

  @Test
  @DisplayName("getPlayersWithBestResult() - empty return")
  final void getPlayerWithBestResult_emptyReturn() throws Exception {
    when(this.playerService.getPlayersWithBestResult())
        .thenReturn(Collections.emptyList());
    this.mockMvc.perform(get("/players/withBestResult")
        .accept(MediaType.APPLICATION_JSON))
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(this.objectMapper.writeValueAsString(Collections.emptyList())));
  }

  @Test
  @DisplayName("create(PlayerSummary) - normal return")
  final void create_normalReturn() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    PlayerSummary savedPlayer = new PlayerSummary(1L, "username", 1);
    when(this.playerService.create(player))
        .thenReturn(savedPlayer);
    this.mockMvc.perform(post("/players")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isCreated(),
            content().contentType(MediaType.APPLICATION_JSON),
            header().string("Location", "/players/" + savedPlayer.getId()),
            content().string(this.objectMapper.writeValueAsString(savedPlayer)));
  }

  @Test
  @DisplayName("create(PlayerSummary) - duplicating username")
  final void create_duplicatingUsername() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    when(this.playerService.create(player))
        .thenThrow(new DuplicateKeyException("message"));
    this.mockMvc.perform(post("/players")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(this.objectMapper.writeValueAsString(Map.of("error", "Duplicating username"))));
  }

  @Test
  @DisplayName("update(Long, PlayerSummary) - normal return")
  @WithMockUser(roles = "USER")
  final void update_normalReturn() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    this.mockMvc.perform(put("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .with(csrf().asHeader())
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isNoContent());
  }

  @Test
  @DisplayName("update(Long, PlayerSummary) - player not found")
  @WithMockUser(roles = "USER")
  final void update_playerNotFound() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    PlayerNotFoundException e = new PlayerNotFoundException(id);
    doAnswer(invocation -> {
      throw e;
    }).when(this.playerService).update(id, player);
    this.mockMvc.perform(put("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .with(csrf().asHeader())
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(
                this.objectMapper.writeValueAsString(Map.of("error", e.getMessage()))));
  }

  @Test
  @DisplayName("update(Long, PlayerSummary) - unauthenticated")
  final void update_unauthenticated() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    this.mockMvc.perform(put("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .with(csrf().asHeader())
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isUnauthorized());
  }

  @Test
  @DisplayName("update(Long, PlayerSummary) - authenticated without role 'USER'")
  @WithMockUser(roles = {})
  final void update_authenticatedWithoutRoleUser() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    this.mockMvc.perform(put("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .with(csrf().asHeader())
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isForbidden());
  }

  @Test
  @DisplayName("update(Long, PlayerSummary) - missing CSRF token")
  @WithMockUser(roles = "USER")
  final void update_missingCsrfToken() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    this.mockMvc.perform(put("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isForbidden());
  }

  @Test
  @DisplayName("update(Long, PlayerSummary) - invalid CSRF token")
  @WithMockUser(roles = "USER")
  final void update_invalidCsrfToken() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    this.mockMvc.perform(put("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .with(csrf().useInvalidToken())
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isForbidden());
  }

  @Test
  @DisplayName("patch(Long, PlayerSummary) - normal return")
  @WithMockUser(roles = "USER")
  final void patch_normalReturn() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    this.mockMvc.perform(patch("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .with(csrf().asHeader())
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isNoContent());
  }

  @Test
  @DisplayName("patch(Long, PlayerSummary) - player not found")
  @WithMockUser(roles = "USER")
  final void patch_playerNotFound() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    PlayerNotFoundException e = new PlayerNotFoundException(id);
    doAnswer(invocation -> {
      throw e;
    }).when(this.playerService).patch(id, player);
    this.mockMvc.perform(patch("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .with(csrf().asHeader())
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            content().string(
                this.objectMapper.writeValueAsString(Map.of("error", e.getMessage()))));
  }

  @Test
  @DisplayName("patch(Long, PlayerSummary) - unauthenticated")
  final void patch_unauthenticated() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    this.mockMvc.perform(patch("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .with(csrf().asHeader())
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isUnauthorized());
  }

  @Test
  @DisplayName("patch(Long, PlayerSummary) - authenticated without role 'USER'")
  @WithMockUser(roles = {})
  final void patch_authenticatedWithoutRoleUser() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    this.mockMvc.perform(patch("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .with(csrf().asHeader())
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isForbidden());
  }

  @Test
  @DisplayName("patch(Long, PlayerSummary) - missing CSRF token")
  @WithMockUser(roles = "USER")
  final void patch_missingCsrfToken() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    this.mockMvc.perform(patch("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isForbidden());
  }

  @Test
  @DisplayName("patch(Long, PlayerSummary) - invalid CSRF token")
  @WithMockUser(roles = "USER")
  final void patch_invalidCsrfToken() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    this.mockMvc.perform(patch("/players/1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .with(csrf().useInvalidToken())
        .content(this.objectMapper.writeValueAsString(player)))
        .andExpectAll(
            status().isForbidden());
  }

  @Test
  @DisplayName("delete(Long) - normal return")
  @WithMockUser(roles = "ADMIN")
  final void delete_normalReturn() throws Exception {
    this.mockMvc.perform(delete("/players/1")
        .with(csrf().asHeader()))
        .andExpectAll(
            status().isNoContent());
  }

  @Test
  @DisplayName("delete(Long) - unauthenticated")
  final void delete_unauthenticated() throws Exception {
    this.mockMvc.perform(delete("/players/1")
        .with(csrf().asHeader()))
        .andExpectAll(
            status().isUnauthorized());
  }

  @Test
  @DisplayName("delete(Long) - authenticated without any role")
  @WithMockUser(roles = {})
  final void delete_withoutAnyRole() throws Exception {
    this.mockMvc.perform(delete("/players/1")
        .with(csrf().asHeader()))
        .andExpectAll(
            status().isForbidden());
  }

  @Test
  @DisplayName("delete(Long) - authenticated with 'USER' role")
  @WithMockUser(roles = "USER")
  final void delete_withUserRole() throws Exception {
    this.mockMvc.perform(delete("/players/1")
        .with(csrf().asHeader()))
        .andExpectAll(
            status().isForbidden());
  }

  @Test
  @DisplayName("delete(Long) - missing CSRF token")
  @WithMockUser(roles = "ADMIN")
  final void delete_missingCsrfToken() throws Exception {
    this.mockMvc.perform(delete("/players/1"))
        .andExpectAll(
            status().isForbidden());
  }

  @Test
  @DisplayName("delete(Long) - invalid CSRF token")
  @WithMockUser(roles = "ADMIN")
  final void delete_invalidCsrfToken() throws Exception {
    this.mockMvc.perform(delete("/players/1")
        .with(csrf().useInvalidToken()))
        .andExpectAll(
            status().isForbidden());
  }

}
