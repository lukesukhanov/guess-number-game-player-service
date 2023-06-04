package com.lukesv.guessnumbergame.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
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

import com.guessnumbergame.api.Application;
import com.guessnumbergame.api.dto.PlayerSummary;
import com.guessnumbergame.api.entity.PlayerEntity;
import com.guessnumbergame.api.exception.PlayerNotFoundException;
import com.guessnumbergame.api.mapper.PlayerMapper;
import com.guessnumbergame.api.repository.PlayerRepository;
import com.guessnumbergame.api.service.PlayerService;

@SpringBootTest(classes = Application.class)
@Tag("service")
@DisplayName("PlayerService")
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
  @DisplayName("getById() - normal return")
  final void getById_normalReturn() throws Exception {
    PlayerSummary player = new PlayerSummary(1L, "username", 1);
    doAnswer(invocation -> {
      assertEquals(invocation.getArgument(0), player.getId());
      return Optional.of(player);
    }).when(this.playerRepository).findPlayerSummaryById(1L);
    assertEquals(this.playerService.getById(1L), player);
  }

  @Test
  @DisplayName("getById() - player not found")
  final void getById_playerNotFound() throws Exception {
    Long id = 1L;
    doAnswer(invocation -> {
      assertEquals(invocation.getArgument(0), id);
      return Optional.empty();
    }).when(this.playerRepository).findPlayerSummaryById(id);
    assertThrows(PlayerNotFoundException.class, () -> this.playerService.getById(id));
  }

  @Test
  @DisplayName("getByUsername() - normal return")
  final void getByUsername_normalReturn() throws Exception {
    PlayerSummary player = new PlayerSummary(1L, "username", 1);
    doAnswer(invocation -> {
      assertEquals(invocation.getArgument(0), player.getUsername());
      return Optional.of(player);
    }).when(this.playerRepository).findPlayerSummaryByUsername(player.getUsername());
    assertEquals(this.playerService.getByUsername(player.getUsername()), player);
  }

  @Test
  @DisplayName("getByUsername() - player not found")
  final void getByUsername_playerNotFound() throws Exception {
    String username = "username";
    doAnswer(invocation -> {
      assertEquals(invocation.getArgument(0), username);
      return Optional.empty();
    }).when(this.playerRepository).findPlayerSummaryByUsername(username);
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
  @DisplayName("create(PlayerSummary player) - normal return")
  final void create_normalReturn() throws Exception {
    PlayerSummary player = new PlayerSummary(1L, "username", 1);
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setUsername(player.getUsername());
    playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
    when(this.playerMapper.playerSummaryToPlayerEntity(player))
        .thenReturn(playerEntity);
    when(this.playerMapper.playerEntityToPlayerSummary(playerEntity))
        .thenReturn(player);
    doAnswer(invocation -> {
      assertEquals(invocation.getArgument(0), playerEntity);
      return playerEntity;
    }).when(this.playerRepository).save(playerEntity);
    assertEquals(this.playerService.create(player), player);
  }

  @Test
  @DisplayName("create(PlayerSummary player) - duplicating username")
  final void create_duplicatingUsername() throws Exception {
    PlayerSummary player = new PlayerSummary(1L, "username", 1);
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setUsername(player.getUsername());
    playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
    when(this.playerMapper.playerSummaryToPlayerEntity(player))
        .thenReturn(playerEntity);
    when(this.playerMapper.playerEntityToPlayerSummary(playerEntity))
        .thenReturn(player);
    doAnswer(invocation -> {
      assertEquals(invocation.getArgument(0), playerEntity);
      throw new DuplicateKeyException("message");
    }).when(this.playerRepository).save(playerEntity);
    assertThrows(DuplicateKeyException.class, () -> this.playerService.create(player));
  }

  @Test
  @DisplayName("update(Long id, PlayerSummary player) - authenticated with matching username")
  @WithMockUser(roles = "USER", username = "username")
  final void update_authenticatedWithMatchingUsername() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    doAnswer(invocation -> {
      PlayerEntity playerEntity = new PlayerEntity();
      playerEntity.setId(id);
      assertEquals(invocation.getArgument(0), id);
      return Optional.of(playerEntity);
    }).when(this.playerRepository).findById(id);
    assertDoesNotThrow(() -> this.playerService.update(id, player));
  }

  @Test
  @DisplayName("update(Long id, PlayerSummary player) - authenticated with role 'ADMIN'")
  @WithMockUser(roles = "ADMIN")
  final void update_authenticatedWithRoleAdmin() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    doAnswer(invocation -> {
      PlayerEntity playerEntity = new PlayerEntity();
      playerEntity.setId(id);
      assertEquals(invocation.getArgument(0), id);
      return Optional.of(playerEntity);
    }).when(this.playerRepository).findById(id);
    assertDoesNotThrow(() -> this.playerService.update(id, player));
  }

  @Test
  @DisplayName("update(Long id, PlayerSummary player) - unauthenticated")
  final void update_unauthenticated() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    assertThrows(AuthenticationCredentialsNotFoundException.class,
        () -> this.playerService.update(id, player));
  }

  @Test
  @DisplayName("update(Long id, PlayerSummary player) - authenticated with not matching username")
  @WithMockUser(roles = "USER", username = "notMatchingUsername")
  final void update_authenticatedWithNotMatchingUsername() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    assertThrows(AccessDeniedException.class,
        () -> this.playerService.update(id, player));
  }

  @Test
  @DisplayName("patch(Long id, PlayerSummary player) - authenticated with matching username")
  @WithMockUser(roles = "USER", username = "username")
  final void patch_authenticatedWithMatchingUsername() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    doAnswer(invocation -> {
      PlayerEntity playerEntity = new PlayerEntity();
      playerEntity.setId(id);
      assertEquals(invocation.getArgument(0), id);
      return Optional.of(playerEntity);
    }).when(this.playerRepository).findById(id);
    assertDoesNotThrow(() -> this.playerService.patch(id, player));
  }

  @Test
  @DisplayName("patch(Long id, PlayerSummary player) - authenticated with role 'ADMIN'")
  @WithMockUser(roles = "ADMIN")
  final void patch_authenticatedWithRoleAdmin() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    doAnswer(invocation -> {
      PlayerEntity playerEntity = new PlayerEntity();
      playerEntity.setId(id);
      assertEquals(invocation.getArgument(0), id);
      return Optional.of(playerEntity);
    }).when(this.playerRepository).findById(id);
    assertDoesNotThrow(() -> this.playerService.patch(id, player));
  }

  @Test
  @DisplayName("patch(Long id, PlayerSummary player) - unauthenticated")
  final void patch_unauthenticated() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    assertThrows(AuthenticationCredentialsNotFoundException.class,
        () -> this.playerService.patch(id, player));
  }

  @Test
  @DisplayName("patch(Long id, PlayerSummary player) - authenticated with not matching username")
  @WithMockUser(roles = "USER", username = "notMatchingUsername")
  final void patch_authenticatedWithNotMatchingUsername() throws Exception {
    Long id = 1L;
    PlayerSummary player = new PlayerSummary(null, "username", 1);
    assertThrows(AccessDeniedException.class,
        () -> this.playerService.patch(id, player));
  }

  @Test
  @DisplayName("delete(Long id) - normal return")
  final void delete_normalReturn() throws Exception {
    Long id = 1L;
    doAnswer(invocation -> {
      assertEquals(invocation.getArgument(0), id);
      return null;
    }).when(this.playerRepository).deleteById(id);
    assertDoesNotThrow(() -> this.playerService.deleteById(id));
  }

}
