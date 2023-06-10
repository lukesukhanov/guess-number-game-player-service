package com.guessnumbergame.playerservice.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import com.guessnumbergame.playerservice.Application;
import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.entity.PlayerEntity;
import com.guessnumbergame.playerservice.exception.PlayerNotFoundException;
import com.guessnumbergame.playerservice.mapper.PlayerMapper;
import com.guessnumbergame.playerservice.repository.PlayerRepository;

@SpringBootTest(classes = Application.class)
@DisplayName("PlayerService")
@Tag("service")
@Tag("player")
class PlayerServiceTest {

  @MockBean
  private PlayerRepository playerRepository;

  @MockBean
  private PlayerMapper playerMapper;

  @Autowired
  private PlayerService playerService;

  private final List<PlayerSummary> existingPlayers;

  {
    List<PlayerSummary> players = new ArrayList<>();
    players.add(new PlayerSummary(1l, "username1", 1));
    players.add(new PlayerSummary(2l, "username2", 2));
    players.add(new PlayerSummary(3l, "username3", 3));
    this.existingPlayers = Collections.unmodifiableList(players);
  }

  @Test
  @DisplayName("getAll() - normal return")
  final void getAll_normalReturn() throws Exception {
    when(this.playerRepository.findAllPlayerSummaries())
        .thenReturn(this.existingPlayers);
    List<PlayerSummary> players = this.playerService.getAll();
    assertIterableEquals(players, this.existingPlayers);
  }

  @Test
  @DisplayName("getAll() - empty return")
  final void getAll_emptyReturn() throws Exception {
    when(this.playerRepository.findAllPlayerSummaries())
        .thenReturn(Collections.emptyList());
    List<PlayerSummary> players = this.playerService.getAll();
    assertIterableEquals(players, Collections.emptyList());
  }

  @Test
  @DisplayName("getById(Long) - normal return")
  final void getById_normalReturn() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(id, "username", 1);
    when(this.playerRepository.findPlayerSummaryById(id))
        .thenReturn(Optional.of(player));
    assertEquals(this.playerService.getById(id), player);
  }

  @Test
  @DisplayName("getById(Long) - player not found")
  final void getById_playerNotFound() throws Exception {
    Long id = 1L;
    when(this.playerRepository.findPlayerSummaryById(id))
        .thenReturn(Optional.empty());
    assertThrows(PlayerNotFoundException.class, () -> this.playerService.getById(id));
  }

  @Test
  @DisplayName("getByUsername(String) - normal return")
  final void getByUsername_normalReturn() throws Exception {
    String username = "username";
    PlayerSummary player = new PlayerSummary(1L, username, 1);
    when(this.playerRepository.findPlayerSummaryByUsername(username))
        .thenReturn(Optional.of(player));
    assertEquals(this.playerService.getByUsername(player.getUsername()), player);
  }

  @Test
  @DisplayName("getByUsername(String) - player not found")
  final void getByUsername_playerNotFound() throws Exception {
    String username = "username";
    when(this.playerRepository.findPlayerSummaryByUsername(username))
        .thenReturn(Optional.empty());
    assertThrows(PlayerNotFoundException.class, () -> this.playerService.getByUsername(username));
  }

  @Test
  @DisplayName("getPlayersWithBestResult() - normal return")
  final void getPlayerWithBestResult_normalReturn() throws Exception {
    when(this.playerRepository.findPlayerSummariesWithBestResult())
        .thenReturn(this.existingPlayers);
    assertEquals(this.playerService.getPlayersWithBestResult(), this.existingPlayers);
  }

  @Test
  @DisplayName("getPlayersWithBestResult() - empty return")
  final void getPlayerWithBestResult_emptyReturn() throws Exception {
    when(this.playerRepository.findPlayerSummariesWithBestResult())
        .thenReturn(Collections.emptyList());
    assertEquals(this.playerService.getPlayersWithBestResult(), Collections.emptyList());
  }

  @Test
  @DisplayName("create(PlayerSummary) - normal return")
  final void create_normalReturn() throws Exception {
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setUsername(player.getUsername());
    playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
    PlayerEntity savedPlayerEntity = new PlayerEntity();
    savedPlayerEntity.setId(1L);
    savedPlayerEntity.setUsername(player.getUsername());
    savedPlayerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
    PlayerSummary savedPlayer = new PlayerSummary(1L, "username", 1);
    when(this.playerMapper.playerSummaryToPlayerEntity(player))
        .thenReturn(playerEntity);
    when(this.playerRepository.save(playerEntity))
        .thenReturn(savedPlayerEntity);
    when(this.playerMapper.playerEntityToPlayerSummary(savedPlayerEntity))
        .thenReturn(savedPlayer);
    assertEquals(this.playerService.create(player), savedPlayer);
  }

  @Test
  @DisplayName("create(PlayerSummary) - duplicating username")
  final void create_duplicatingUsername() throws Exception {
    PlayerSummary player = new PlayerSummary(1L, "username", 1);
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setUsername(player.getUsername());
    playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
    when(this.playerMapper.playerSummaryToPlayerEntity(player))
        .thenReturn(playerEntity);
    when(this.playerRepository.save(playerEntity))
        .thenThrow(new DuplicateKeyException("message"));
    assertThrows(DuplicateKeyException.class, () -> this.playerService.create(player));
  }

  @Test
  @DisplayName("update(Long, PlayerSummary) - authenticated with matching username")
  @WithMockUser(roles = "USER", username = "username")
  final void update_authenticatedWithMatchingUsername() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setId(id);
    when(this.playerRepository.findById(id))
        .thenReturn(Optional.of(playerEntity));
    assertDoesNotThrow(() -> this.playerService.update(id, player));
  }

  @Test
  @DisplayName("update(Long, PlayerSummary) - authenticated with role 'ADMIN'")
  @WithMockUser(roles = "ADMIN")
  final void update_authenticatedWithRoleAdmin() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setId(id);
    when(this.playerRepository.findById(id))
        .thenReturn(Optional.of(playerEntity));
    assertDoesNotThrow(() -> this.playerService.update(id, player));
  }

  @Test
  @DisplayName("update(Long, PlayerSummary) - unauthenticated")
  final void update_unauthenticated() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    assertThrows(AuthenticationCredentialsNotFoundException.class,
        () -> this.playerService.update(id, player));
  }

  @Test
  @DisplayName("update(Long, PlayerSummary) - authenticated with not matching username")
  @WithMockUser(roles = "USER", username = "notMatchingUsername")
  final void update_authenticatedWithNotMatchingUsername() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    assertThrows(AccessDeniedException.class,
        () -> this.playerService.update(id, player));
  }

  @Test
  @DisplayName("patch(Long, PlayerSummary) - authenticated with matching username")
  @WithMockUser(roles = "USER", username = "username")
  final void patch_authenticatedWithMatchingUsername() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setId(id);
    when(this.playerRepository.findById(id))
        .thenReturn(Optional.of(playerEntity));
    assertDoesNotThrow(() -> this.playerService.patch(id, player));
  }

  @Test
  @DisplayName("patch(Long, PlayerSummary) - authenticated with role 'ADMIN'")
  @WithMockUser(roles = "ADMIN")
  final void patch_authenticatedWithRoleAdmin() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setId(id);
    when(this.playerRepository.findById(id))
        .thenReturn(Optional.of(playerEntity));
    assertDoesNotThrow(() -> this.playerService.patch(id, player));
  }

  @Test
  @DisplayName("patch(Long, PlayerSummary) - unauthenticated")
  final void patch_unauthenticated() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    assertThrows(AuthenticationCredentialsNotFoundException.class,
        () -> this.playerService.patch(id, player));
  }

  @Test
  @DisplayName("patch(Long, PlayerSummary) - authenticated with not matching username")
  @WithMockUser(roles = "USER", username = "notMatchingUsername")
  final void patch_authenticatedWithNotMatchingUsername() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    assertThrows(AccessDeniedException.class,
        () -> this.playerService.patch(id, player));
  }

  @Test
  @DisplayName("delete(Long) - normal return")
  final void delete_normalReturn() throws Exception {
    Long id = 1L;
    assertDoesNotThrow(() -> this.playerService.deleteById(id));
  }

}
