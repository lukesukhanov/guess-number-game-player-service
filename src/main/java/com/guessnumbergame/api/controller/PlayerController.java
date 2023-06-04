package com.guessnumbergame.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guessnumbergame.api.dto.PlayerSummary;
import com.guessnumbergame.api.exception.PlayerNotFoundException;
import com.guessnumbergame.api.exception.PlayerNotUpdatedException;
import com.guessnumbergame.api.exception.handler.PlayerResponseEntityExceptionHandler;
import com.guessnumbergame.api.service.PlayerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Provides the REST endpoints to manage players of the game.
 * <p>
 * {@link PlayerSummary} objects are managed with the help of
 * {@link PlayerService}.
 * <p>
 * The JSON format is used for the response bodies.
 * 
 * @see PlayerSummary
 * @see PlayerService
 */
@RestController
@RequestMapping(path = "/players", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PlayerController {

  private final PlayerService playerService;

  /**
   * Responds with information about all the players.
   * <p>
   * Processes {@code GET /players} requests.
   * 
   * @return a {@code ResponseEntity} with status 200 and the body containing
   *         all the players found
   */
  @GetMapping
  public ResponseEntity<List<PlayerSummary>> getAll() {
    List<PlayerSummary> players = this.playerService.getAll();
    return ResponseEntity.ok(players);
  }

  /**
   * Responds with information about the player with the given id.
   * <p>
   * Processes {@code GET /players/{id}} requests.
   * 
   * @param id a {@code Long} representing player's id
   * @return a {@code ResponseEntity} with status 200 and the body containing
   *         the player with the given id
   * @throws PlayerNotFoundException if the player with the given id doesn't
   *         exist
   * @see PlayerResponseEntityExceptionHandler
   */
  @GetMapping("/{id}")
  public ResponseEntity<PlayerSummary> getById(@PathVariable Long id) {
    PlayerSummary player = this.playerService.getById(id);
    return ResponseEntity.ok(player);
  }

  /**
   * Responds with information about the player with the given username.
   * <p>
   * Processes {@code GET /players} requests with {@code username} parameter.
   * 
   * @param username a {@code String} representing player's username
   * @return a {@code ResponseEntity} with status 200 and the body containing
   *         the player with the given
   *         username
   * @throws PlayerNotFoundException if the player with the given username
   *         doesn't exist
   * @see PlayerResponseEntityExceptionHandler
   */
  @GetMapping(params = "username")
  public ResponseEntity<PlayerSummary> getByUsername(@RequestParam String username) {
    PlayerSummary player = this.playerService.getByUsername(username);
    return ResponseEntity.ok(player);
  }

  /**
   * Responds with information about the players with the best result.
   * <p>
   * Processes {@code GET /players/withBestResult} requests.
   * 
   * @return a {@code ResponseEntity} with status 200 and the body containing
   *         the players with the best result
   */
  @GetMapping("/withBestResult")
  public ResponseEntity<List<PlayerSummary>> getPlayersWithBestResult() {
    return ResponseEntity.ok(this.playerService.getPlayersWithBestResult());
  }

  /**
   * Saves the given player, responds with information about the player saved.
   * <p>
   * Processes {@code POST /players} requests with {@code PlayerSummary} body.
   * 
   * @param player a {@code PlayerSummary} representing the player to be saved
   * @return a {@code ResponseEntity} with status 201 and the body containing
   *         the saved player
   * @throws DuplicateKeyException if the player with the given username already
   *         exist
   * @see PlayerResponseEntityExceptionHandler
   */
  @PostMapping
  public ResponseEntity<PlayerSummary> create(@Valid @RequestBody PlayerSummary player) {
    PlayerSummary savedPlayer = this.playerService.create(player);
    return ResponseEntity
        .created(URI.create("/players/" + savedPlayer.getId()))
        .body(savedPlayer);
  }

  /**
   * Updates the player with the given id, responds with no content.
   * <p>
   * Processes {@code PUT /players/{id}} requests with {@code PlayerSummary}
   * body.
   * 
   * @param id a {@code Long} representing the player's id
   * @param player a {@code PlayerSummary} representing the player details to be
   *        updated
   * @return a {@code ResponseEntity} with status 204 and no content
   * @throws PlayerNotFoundException if the player with the given id doesn't
   *         exist
   * @throws PlayerNotUpdatedException if the player failed to be updated for
   *         technical reasons
   * @see PlayerResponseEntityExceptionHandler
   */
  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody PlayerSummary player) {
    this.playerService.update(id, player);
    return ResponseEntity.noContent().build();
  }

  /**
   * Patches the player with the given id, responds with no content.
   * <p>
   * Processes {@code PATCH /players/{id}} requests with {@code PlayerSummary}
   * body.
   * 
   * @param id a {@code Long} representing the player's id
   * @param player a {@code PlayerSummary} representing the player details to be
   *        patched
   * @return a {@code ResponseEntity} with status 204 and no content
   * @throws PlayerNotFoundException if the player with the given id doesn't
   *         exist
   * @throws PlayerNotUpdatedException if the player failed to be patched for
   *         technical reasons
   * @see PlayerResponseEntityExceptionHandler
   */
  @PatchMapping("/{id}")
  public ResponseEntity<?> patch(@PathVariable Long id, @RequestBody PlayerSummary player) {
    this.playerService.patch(id, player);
    return ResponseEntity.noContent().build();
  }

  /**
   * Deletes the player with the given id, responds with no content.
   * <p>
   * Processes {@code DELETE /players/{id}} requests.
   * 
   * @param id a {@code Long} representing the player's id
   * @return a {@code ResponseEntity} with status 204 and no content
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable Long id) {
    this.playerService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
