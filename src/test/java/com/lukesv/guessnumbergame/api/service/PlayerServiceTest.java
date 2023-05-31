package com.lukesv.guessnumbergame.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import com.lukesv.guessnumbergame.api.Application;
import com.lukesv.guessnumbergame.api.dto.PlayerSummary;
import com.lukesv.guessnumbergame.api.entity.PlayerEntity;
import com.lukesv.guessnumbergame.api.exception.PlayerNotFoundException;
import com.lukesv.guessnumbergame.api.mapper.PlayerMapper;
import com.lukesv.guessnumbergame.api.repository.PlayerRepository;

@SpringBootTest(classes = Application.class)
@Tag("service")
@DisplayName("PlayerService")
class PlayerServiceTest {

	private static final String DEFAULT_USERNAME = "username";

	@MockBean
	private PlayerRepository playerRepository;

	@MockBean
	private PlayerMapper playerMapper;

	@Autowired
	private PlayerService playerService;

	private final List<PlayerSummary> players;

	private final PlayerSummary player = new PlayerSummary(1L, DEFAULT_USERNAME, 1);

	PlayerServiceTest() {
		List<PlayerSummary> players = new ArrayList<>();
		players.add(new PlayerSummary(1l, "username1", 1));
		players.add(new PlayerSummary(2l, "username2", 2));
		players.add(new PlayerSummary(3l, "username3", 3));
		this.players = Collections.unmodifiableList(players);
	}

	@Test
	@DisplayName("getAll() - normal return")
	final void getAll_normalReturn() throws Exception {
		when(this.playerRepository.findAllPlayerSummaries()).thenReturn(this.players);
		List<PlayerSummary> players = this.playerService.getAll();
		assertIterableEquals(players, this.players);
	}

	@Test
	@DisplayName("getAll() - empty return")
	final void getAll_emptyReturn() throws Exception {
		when(this.playerRepository.findAllPlayerSummaries()).thenReturn(Collections.emptyList());
		List<PlayerSummary> players = this.playerService.getAll();
		assertIterableEquals(players, Collections.emptyList());
	}

	@Test
	@DisplayName("getById() - normal return")
	final void getById_normalReturn() throws Exception {
		when(this.playerRepository.findPlayerSummaryById(this.player.getId()))
				.thenReturn(Optional.of(this.player));
		assertEquals(this.playerService.getById(1L), this.player);
	}

	@Test
	@DisplayName("getById() - player not found")
	final void getById_playerNotFound() throws Exception {
		when(this.playerRepository.findPlayerSummaryById(1L)).thenReturn(Optional.empty());
		assertThrows(PlayerNotFoundException.class, () -> this.playerService.getById(1L));
	}

	@Test
	@DisplayName("getByUsername() - normal return")
	final void getByUsername_normalReturn() throws Exception {
		when(this.playerRepository.findPlayerSummaryByUsername("username")).thenReturn(Optional.of(this.player));
		assertEquals(this.playerService.getByUsername("username"), this.player);
	}

	@Test
	@DisplayName("getByUsername() - player not found")
	final void getByUsername_playerNotFound() throws Exception {
		when(this.playerRepository.findPlayerSummaryByUsername("username")).thenReturn(Optional.empty());
		assertThrows(PlayerNotFoundException.class, () -> this.playerService.getByUsername("username"));
	}

	@Test
	@DisplayName("getPlayersWithBestResult() - normal return")
	final void getPlayerWithBestResult_normalReturn() throws Exception {
		when(this.playerRepository.findPlayerSummariesWithBestResult()).thenReturn(this.players);
		assertEquals(this.playerService.getPlayersWithBestResult(), this.players);
	}

	@Test
	@DisplayName("getPlayersWithBestResult() - empty return")
	final void getPlayerWithBestResult_emptyReturn() throws Exception {
		when(this.playerRepository.findPlayerSummariesWithBestResult()).thenReturn(Collections.emptyList());
		assertEquals(this.playerService.getPlayersWithBestResult(), Collections.emptyList());
	}

	@Test
	@DisplayName("create() - normal return")
	final void create_normalReturn() throws Exception {
		PlayerEntity playerEntity = new PlayerEntity();
		playerEntity.setUsername(this.player.getUsername());
		playerEntity.setBestAttemptsCount(this.player.getBestAttemptsCount());
		when(this.playerMapper.playerSummaryToPlayerEntity(this.player)).thenReturn(playerEntity);
		when(this.playerRepository.save(playerEntity)).thenReturn(playerEntity);
		when(this.playerMapper.playerEntityToPlayerSummary(playerEntity)).thenReturn(this.player);
		assertEquals(this.playerService.create(this.player), this.player);
	}

	@Test
	@DisplayName("create() - duplicating username")
	final void create_duplicatingUsername() throws Exception {
		PlayerEntity playerEntity = new PlayerEntity();
		playerEntity.setUsername(this.player.getUsername());
		playerEntity.setBestAttemptsCount(this.player.getBestAttemptsCount());
		when(this.playerMapper.playerSummaryToPlayerEntity(this.player)).thenReturn(playerEntity);
		when(this.playerMapper.playerEntityToPlayerSummary(playerEntity)).thenReturn(this.player);
		when(this.playerRepository.save(playerEntity)).thenThrow(DuplicateKeyException.class);
		assertThrows(DuplicateKeyException.class, () -> this.playerService.create(this.player));
	}

	@Test
	@DisplayName("update() - authenticated with matching username")
	@WithMockUser(roles = "USER", username = DEFAULT_USERNAME)
	final void update_authenticatedWithMatchingUsername() throws Exception {
		PlayerEntity playerEntity = new PlayerEntity();
		playerEntity.setId(this.player.getId());
		when(this.playerRepository.findById(this.player.getId())).thenReturn(Optional.of(playerEntity));
		this.playerService.update(this.player.getId(), this.player);
	}

	@Test
	@DisplayName("update() - authenticated with role 'ADMIN'")
	@WithMockUser(roles = "ADMIN")
	final void update_authenticatedWithRoleAdmin() throws Exception {
		PlayerEntity playerEntity = new PlayerEntity();
		playerEntity.setId(this.player.getId());
		when(this.playerRepository.findById(this.player.getId())).thenReturn(Optional.of(playerEntity));
		this.playerService.update(this.player.getId(), this.player);
	}

	@Test
	@DisplayName("update() - unauthenticated")
	final void update_unauthenticated() throws Exception {
		PlayerEntity playerEntity = new PlayerEntity();
		playerEntity.setId(this.player.getId());
		when(this.playerRepository.findById(this.player.getId())).thenReturn(Optional.of(playerEntity));
		assertThrows(AuthenticationCredentialsNotFoundException.class,
				() -> this.playerService.update(this.player.getId(), this.player));
	}

}
