package com.lukesv.guessnumbergame.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.guessnumbergame.api.Application;
import com.guessnumbergame.api.dto.PlayerSummary;
import com.guessnumbergame.api.exception.PlayerNotFoundException;
import com.guessnumbergame.api.service.PlayerService;

@SpringBootTest(classes = Application.class)
@Tag("controller")
@DisplayName("PlayerController")
@AutoConfigureMockMvc
class PlayerControllerTest {

	private static final List<PlayerSummary> expectedPlayers;

	static {
		List<PlayerSummary> players = new ArrayList<>();
		players.add(new PlayerSummary(1l, "username1", 1));
		players.add(new PlayerSummary(2l, "username2", 2));
		players.add(new PlayerSummary(3l, "username3", 3));
		expectedPlayers = Collections.unmodifiableList(players);
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PlayerService playerService;

	@Test
	@DisplayName("getAll() - normal return")
	final void getAll_normalReturn() throws Exception {
		when(this.playerService.getAll()).thenReturn(expectedPlayers);
		this.mockMvc.perform(get("/players")
				.accept(MediaType.APPLICATION_JSON))
				.andExpectAll(
						status().isOk(),
						content().contentType(MediaType.APPLICATION_JSON),
						content().string(this.objectMapper.writeValueAsString(this.playerService.getAll())));
	}

	@Test
	@DisplayName("getAll() - normal empty return")
	final void getAll_emptyReturn() throws Exception {
		when(this.playerService.getAll()).thenReturn(Collections.emptyList());
		this.mockMvc.perform(get("/players")
				.accept(MediaType.APPLICATION_JSON))
				.andExpectAll(
						status().isOk(),
						content().contentType(MediaType.APPLICATION_JSON),
						content().string(this.objectMapper.writeValueAsString(Collections.emptyList())));
	}

	@Test
	@DisplayName("getById() - normal return")
	final void getById_normalReturn() throws Exception {
		PlayerSummary player = new PlayerSummary(1L, "username", 1);
		when(this.playerService.getById(1L)).thenReturn(player);
		this.mockMvc.perform(get("/players/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpectAll(
						status().isOk(),
						content().contentType(MediaType.APPLICATION_JSON),
						content().string(this.objectMapper.writeValueAsString(player)));
	}

	@Test
	@DisplayName("getById() - player not found")
	final void getById_playerNotFound() throws Exception {
		PlayerNotFoundException e = new PlayerNotFoundException(1L);
		when(this.playerService.getById(1L)).thenThrow(e);
		this.mockMvc.perform(get("/players/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpectAll(
						status().isNotFound(),
						content().contentType(MediaType.APPLICATION_JSON),
						content().json(this.objectMapper.writeValueAsString(Map.of("error", e.getMessage()))));
	}

	@Test
	@DisplayName("getByUsername() - normal return")
	final void getByUsername_normalReturn() throws Exception {
		PlayerSummary player = new PlayerSummary(1L, "username", 1);
		when(this.playerService.getByUsername("username")).thenReturn(player);
		this.mockMvc.perform(get("/players?username=username")
				.accept(MediaType.APPLICATION_JSON))
				.andExpectAll(
						status().isOk(),
						content().contentType(MediaType.APPLICATION_JSON),
						content().string(this.objectMapper.writeValueAsString(player)));
	}

	@Test
	@DisplayName("getByUsername() - player not found")
	final void getByUsername_playerNotFound() throws Exception {
		PlayerNotFoundException e = new PlayerNotFoundException("username");
		when(this.playerService.getByUsername("username")).thenThrow(e);
		this.mockMvc.perform(get("/players?username=username")
				.accept(MediaType.APPLICATION_JSON))
				.andExpectAll(
						status().isNotFound(),
						content().contentType(MediaType.APPLICATION_JSON),
						content().json(this.objectMapper.writeValueAsString(Map.of("error", e.getMessage()))));
	}

	@Test
	@DisplayName("getPlayersWithBestResult() - normal return")
	final void getPlayerWithBestResult_normalReturn() throws Exception {
		int bestResult = expectedPlayers.stream()
				.mapToInt(PlayerSummary::getBestAttemptsCount)
				.min()
				.getAsInt();
		List<PlayerSummary> playersWithBestResult = expectedPlayers.stream()
				.filter(player -> player.getBestAttemptsCount() == bestResult)
				.toList();
		when(this.playerService.getPlayersWithBestResult()).thenReturn(playersWithBestResult);
		this.mockMvc.perform(get("/players/withBestResult")
				.accept(MediaType.APPLICATION_JSON))
				.andExpectAll(
						status().isOk(),
						content().contentType(MediaType.APPLICATION_JSON),
						content().string(this.objectMapper.writeValueAsString(playersWithBestResult)));
	}

	@Test
	@DisplayName("getPlayersWithBestResult() - empty return")
	final void getPlayerWithBestResult_emptyReturn() throws Exception {
		when(this.playerService.getPlayersWithBestResult()).thenReturn(Collections.emptyList());
		this.mockMvc.perform(get("/players/withBestResult")
				.accept(MediaType.APPLICATION_JSON))
				.andExpectAll(
						status().isOk(),
						content().contentType(MediaType.APPLICATION_JSON),
						content().string(this.objectMapper.writeValueAsString(Collections.emptyList())));
	}

	@Test
	@DisplayName("create() - normal return")
	final void create_normalReturn() throws Exception {
		PlayerSummary player = new PlayerSummary(1L, "username", 1);
		when(this.playerService.create(player)).thenReturn(player);
		this.mockMvc.perform(post("/players")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(player)))
				.andExpectAll(
						status().isCreated(),
						content().contentType(MediaType.APPLICATION_JSON),
						header().string("Location", "/players/1"),
						content().string(this.objectMapper.writeValueAsString(player)));
	}

	@Test
	@DisplayName("create() - duplicating username")
	final void create_duplicatingUsername() throws Exception {
		PlayerSummary player = new PlayerSummary(1L, "username", 1);
		when(this.playerService.create(player)).thenThrow(DuplicateKeyException.class);
		this.mockMvc.perform(post("/players")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(player)))
				.andExpectAll(
						status().isBadRequest(),
						content().contentType(MediaType.APPLICATION_JSON),
						content().string(this.objectMapper.writeValueAsString(Map.of("error", "Duplicate"))));
	}

	@Test
	@DisplayName("update() - normal return")
	@WithMockUser(roles = "USER")
	final void update_normalReturn() throws Exception {
		PlayerSummary player = new PlayerSummary(1L, "username", 1);
		when(this.playerService.create(player)).thenReturn(player);
		this.mockMvc.perform(put("/players/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(csrf().asHeader())
				.content(this.objectMapper.writeValueAsString(player)))
				.andExpectAll(
						status().isNoContent());
	}

	@Test
	@DisplayName("update() - unauthenticated")
	final void update_unauthenticated() throws Exception {
		PlayerSummary player = new PlayerSummary(1L, "username", 1);
		when(this.playerService.create(player)).thenReturn(player);
		this.mockMvc.perform(put("/players/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(csrf().asHeader())
				.content(this.objectMapper.writeValueAsString(player)))
				.andExpectAll(
						status().isUnauthorized());
	}

	@Test
	@DisplayName("update() - missing CSRF token")
	@WithMockUser(roles = "USER")
	final void update_missingCsrfToken() throws Exception {
		PlayerSummary player = new PlayerSummary(1L, "username", 1);
		when(this.playerService.create(player)).thenReturn(player);
		this.mockMvc.perform(put("/players/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(player)))
				.andExpectAll(
						status().isForbidden());
	}

	@Test
	@DisplayName("update() - invalid CSRF token")
	@WithMockUser(roles = "USER")
	final void update_invalidCsrfToken() throws Exception {
		PlayerSummary player = new PlayerSummary(1L, "username", 1);
		when(this.playerService.create(player)).thenReturn(player);
		this.mockMvc.perform(put("/players/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(csrf().useInvalidToken())
				.content(this.objectMapper.writeValueAsString(player)))
				.andExpectAll(
						status().isForbidden());
	}

	@Test
	@DisplayName("update() - authenticated without role 'USER'")
	@WithMockUser(roles = {})
	final void update_authenticatedWithoutRoleUser() throws Exception {
		PlayerSummary player = new PlayerSummary(1L, "username", 1);
		when(this.playerService.create(player)).thenReturn(player);
		this.mockMvc.perform(put("/players/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(csrf().asHeader())
				.content(this.objectMapper.writeValueAsString(player)))
				.andExpectAll(
						status().isForbidden());
	}

}
