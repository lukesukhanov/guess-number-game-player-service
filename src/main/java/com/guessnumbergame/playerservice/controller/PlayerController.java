package com.guessnumbergame.playerservice.controller;

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

import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.exception.PlayerNotFoundException;
import com.guessnumbergame.playerservice.exception.PlayerNotUpdatedException;
import com.guessnumbergame.playerservice.exception.handler.PlayerResponseEntityExceptionHandler;
import com.guessnumbergame.playerservice.service.PlayerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Provides the endpoints to access players of the game.<br />
 * <p>
 * The endpoints {@code /players/**} are used.
 * <p>
 * The JSON format is used for the response body.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see PlayerSummary
 * @see PlayerService
 */
@RestController
@RequestMapping(path = "/players", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PlayerController {

  private final PlayerService playerService;

  /**
   * Finds all existing players.
   * <p>
   * Serves the {@code GET} requests for the {@code /players} endpoint.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * GET /players<br />
   * <p>
   * <i>Normal response</i>
   * <p>
   * Status: 200<br />
   * Body: [{id: 1, username: "vasya", bestAttemptsCount: 10}, ... ]
   * 
   * @return a {@code ResponseEntity} with the status {@code 200} and the body
   *         containing all the players found
   */
  @GetMapping
  public ResponseEntity<List<PlayerSummary>> getAll() {
    List<PlayerSummary> players = this.playerService.getAll();
    return ResponseEntity.ok(players);
  }

  /**
   * Finds a player by id.
   * <p>
   * Serves the {@code GET} requests for the {@code /players{id}} endpoints.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * GET /players/1<br />
   * <p>
   * <i>The player was found</i>
   * <p>
   * Status: 200<br />
   * Body: {id: 1, username: "vasya", bestAttemptsCount: 10}
   * <p>
   * <i>The player wasn't found</i>
   * <p>
   * Status: 404<br />
   * Body: {error: "Can't find player with id = 1"}
   * 
   * @param id a {@code Long} representing the player's id
   * @return a {@code ResponseEntity} with the status {@code 200} and the body
   *         containing the player with the given id
   * @throws PlayerNotFoundException if the player with this id doesn't exist
   * @see PlayerResponseEntityExceptionHandler
   */
  @GetMapping("/{id}")
  public ResponseEntity<PlayerSummary> getById(@PathVariable Long id) {
    PlayerSummary player = this.playerService.getById(id);
    return ResponseEntity.ok(player);
  }

  /**
   * Finds a player by username.
   * <p>
   * Serves the {@code GET} requests for the
   * {@code /players/byUsername?username={username}} endpoints.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * GET /players?username=vasya<br />
   * <p>
   * <i>The player was found</i>
   * <p>
   * Status: 200<br />
   * Body: {id: 1, username: "vasya", bestAttemptsCount: 10}
   * <p>
   * <i>The player wasn't found</i>
   * <p>
   * Status: 404<br />
   * Body: {error: "Can't find player with username 'vasya'"}
   * 
   * @param username a {@code String} representing the player's username
   * @return a {@code ResponseEntity} with the status {@code 200} and the body
   *         containing the player with the given username
   * @throws PlayerNotFoundException if the player with this username doesn't
   *         exist
   * @see PlayerResponseEntityExceptionHandler
   */
  @GetMapping(path = "/byUsername")
  public ResponseEntity<PlayerSummary> getByUsername(@RequestParam("username") String username) {
    PlayerSummary player = this.playerService.getByUsername(username);
    return ResponseEntity.ok(player);
  }

  /**
   * Finds all players with the best result.
   * <p>
   * Serves the {@code GET} requests for the {@code /players/withBestResult}
   * endpoint.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * GET /players/withBestResult<br />
   * <p>
   * <i>Normal response</i>
   * <p>
   * Status: 200<br />
   * Body: [{id: 1, username: "vasya", bestAttemptsCount: 10}, ... ]
   * 
   * @return a {@code ResponseEntity} with the status {@code 200} and the body
   *         containing all players with the best result
   */
  @GetMapping("/withBestResult")
  public ResponseEntity<List<PlayerSummary>> getPlayersWithBestResult() {
    return ResponseEntity.ok(this.playerService.getPlayersWithBestResult());
  }

  /**
   * Creates a new player.<br />
   * The player's id from the request body is ignored.
   * <p>
   * Serves the {@code POST} requests for the {@code /players} endpoint.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * POST /players<br />
   * Body: {id: 1, username: "vasya", bestAttemptsCount: 10}
   * <p>
   * <i>A new player was created</i>
   * <p>
   * Status: 201<br />
   * Location: /players/1<br />
   * Body: {id: 1, username: "vasya", bestAttemptsCount: 10}
   * <p>
   * <i>The player with this username already exists</i>
   * <p>
   * Status: 400<br />
   * Body: {error: "Duplicating username"}
   * 
   * @param player a {@code PlayerSummary} representing the player to create
   * @return a {@code ResponseEntity} with the status {@code 201} and the body
   *         containing a new player
   * @throws DuplicateKeyException if the player with this username already
   *         exists
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
   * Updates the player by id. Requires authentication and the CSRF token.<br />
   * The player's id from the request body is ignored.
   * <p>
   * Serves the {@code PUT} requests for the {@code /players/{id}} endpoints.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * PUT /players/1<br />
   * Body: {id: 1, username: "vasya", bestAttemptsCount: 10}
   * <p>
   * <i>The player was updated</i>
   * <p>
   * Status: 204
   * <p>
   * <i>The player wasn't found</i>
   * <p>
   * Status: 404<br />
   * Body: {error: "Can't find player with id = 1}
   * <p>
   * <i>The player with this username already exists</i>
   * <p>
   * Status: 400<br />
   * Body: {error: "Duplicating username"}
   * 
   * @param id a {@code Long} representing the player's id
   * @param player a {@code PlayerSummary} representing the player to be updated
   * @return a {@code ResponseEntity} with the status {@code 204}
   * @throws PlayerNotFoundException if the player with this id doesn't exist
   * @throws DuplicateKeyException if the player with this username already
   *         exists
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
   * Patches the player by id. Requires authentication and the CSRF token.<br />
   * The player's id from the request body is ignored.<br />
   * <p>
   * Serves the {@code PATCH} requests for the {@code /players/{id}} endpoints.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * PATCH /players/[id]<br />
   * Body: {id: 1, username: "vasya", bestAttemptsCount: 10}
   * <p>
   * <i>The player was patched</i>
   * <p>
   * Status: 204
   * <p>
   * <i>The player wasn't found</i>
   * <p>
   * Status: 404<br />
   * Body: {error: "Can't find player with id = 1"}
   * <p>
   * <i>The player with this username already exists</i>
   * <p>
   * Status: 400<br />
   * Body: {error: "Duplicating username"}
   * 
   * @param id a {@code Long} representing the player's id
   * @param player a {@code PlayerSummary} representing the player to be patched
   * @return a {@code ResponseEntity} with the status {@code 204}
   * @throws PlayerNotFoundException if the player with this id doesn't exist
   * @throws DuplicateKeyException if the player with this username already
   *         exists
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
   * Deletes the player by id. Requires authentication and the CSRF token.
   * <p>
   * Serves the {@code DELETE} requests for the {@code /players/{id}} endpoints.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * DELETE /players/1<br />
   * <p>
   * <i>The player was deleted</i>
   * <p>
   * Status: 204
   * 
   * @param id a {@code Long} representing the player's id
   * @return a {@code ResponseEntity} with the status {@code 204}
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable Long id) {
    this.playerService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
