package com.lukesv.guessnumbergame.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import com.guessnumbergame.api.Application;
import com.guessnumbergame.api.dto.PlayerSummary;
import com.guessnumbergame.api.repository.PlayerRepository;
import com.lukesv.guessnumbergame.api.config.test.PlayerRepositoryTestConfig;

@SpringBootTest(classes = Application.class)
@Tag("repository")
@DisplayName("PlayerRepository")
@Import(PlayerRepositoryTestConfig.class)
class PlayerRepositoryTest {

  @Autowired
  private PlayerRepository playerRepository;

  private final List<PlayerSummary> existingPlayers;

  private final List<PlayerSummary> existingPlayersWithBestResult;

  {
    PlayerSummary player1 = new PlayerSummary(null, "ivan", 8);
    PlayerSummary player2 = new PlayerSummary(null, "pyotr", 5);
    PlayerSummary player3 = new PlayerSummary(null, "nadezhda", 7);
    PlayerSummary player4 = new PlayerSummary(null, "boris", 5);
    PlayerSummary player5 = new PlayerSummary(null, "darya", null);

    List<PlayerSummary> players = new ArrayList<>(5);
    Collections.addAll(players, player1, player2, player3, player4, player5);
    this.existingPlayers = Collections.unmodifiableList(players);

    List<PlayerSummary> playersWithBestResult = new ArrayList<>(2);
    Collections.addAll(playersWithBestResult, player2, player4);
    this.existingPlayersWithBestResult = Collections.unmodifiableList(playersWithBestResult);
  }

  @Test
  @DisplayName("findAllPlayerSummaries() - normal return")
  final void findAllPlayerSummaries_normalReturn() {
    List<PlayerSummary> players = this.playerRepository.findAllPlayerSummaries();
    assertTrue(
        players.containsAll(this.existingPlayers) && this.existingPlayers.containsAll(players));
  }

  @Test
  @DisplayName("findPlayerSummaryById(Long id) - normal return")
  final void findPlayerSummaryById_normalReturn() {
    PlayerSummary player = this.playerRepository.findPlayerSummaryById(1L).get();
    assertEquals(player, this.existingPlayers.get(0));
  }

  @Test
  @DisplayName("findPlayerSummaryById(Long id) - empty return")
  final void findPlayerSummaryById_emptyReturn() {
    Optional<PlayerSummary> player = this.playerRepository.findPlayerSummaryById(0L);
    assertTrue(player.isEmpty());
  }

  @Test
  @DisplayName("findPlayerSummaryByUsername(String username) - normal return")
  final void findPlayerSummaryByUsername_normalReturn() {
    PlayerSummary player = this.playerRepository.findPlayerSummaryByUsername("ivan").get();
    assertEquals(player, this.existingPlayers.get(0));
  }

  @Test
  @DisplayName("findPlayerSummaryByUsername(String username) - empty return")
  final void findPlayerSummaryByUsername_emptyReturn() {
    Optional<PlayerSummary> player = this.playerRepository
        .findPlayerSummaryByUsername("notExistingVasiliy");
    assertTrue(player.isEmpty());
  }

  @Test
  @DisplayName("findPlayerSummariesWithBestResult() - normal return")
  final void findPlayerSummariesWithBestResult_normalReturn() {
    List<PlayerSummary> playersWithBestResult = this.playerRepository
        .findPlayerSummariesWithBestResult();
    assertTrue(playersWithBestResult.containsAll(this.existingPlayersWithBestResult)
        && this.existingPlayersWithBestResult.containsAll(playersWithBestResult));
  }

  @Test
  @DisplayName("deleteById(Long id) - normal return")
  @DirtiesContext
  final void deleteById_normalReturn() {
    Long id = 1L;
    Optional<PlayerSummary> player = this.playerRepository.findPlayerSummaryById(id);
    assertTrue(player.isPresent());
    this.playerRepository.deleteById(id);
    player = this.playerRepository.findPlayerSummaryById(id);
    assertTrue(player.isEmpty());
  }

}
