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
 * The REST controller which provides the endpoints to access information about
 * players of the game.<br />
 * Works with players using {@link PlayerSummary} objects and the
 * {@link PlayerService} service.
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
   * Body: [{"id": "[id]", "username": "[username]", "bestAttemptsCount":
   * "[bestAttemptsCount]"}, ...]
   * 
   * @return a {@code ResponseEntity} with the {@code 200} status and the body
   *         containing all the players found
   */
  @GetMapping
  public ResponseEntity<List<PlayerSummary>> getAll() {
    List<PlayerSummary> players = this.playerService.getAll();
    return ResponseEntity.ok(players);
  }

  /**
   * Finds the player with the given id.
   * <p>
   * Serves the {@code GET} requests for the {@code /players{id}} endpoints.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * GET /players/[id]<br />
   * <p>
   * <i>Normal response</i>
   * <p>
   * Status: 200<br />
   * Body: {"id": "[id]", "username": "[username]", "bestAttemptsCount":
   * "[bestAttemptsCount]"}
   * <p>
   * <i>Response in case the player not found</i>
   * <p>
   * Status: 404<br />
   * Body: {"error": "Can't find player with id = [id]"}
   * 
   * @param id a {@code Long} representing the player's id
   * @return a {@code ResponseEntity} with the {@code 200} status and the body
   *         containing information about the player with the given id
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
   * Serves the {@code GET} requests for the
   * {@code /players?username=[username]} endpoints.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * GET /players?username=[username]<br />
   * <p>
   * <i>Normal response</i>
   * <p>
   * Status: 200<br />
   * Body: {"id": "[id]", "username": "[username]", "bestAttemptsCount":
   * "[bestAttemptsCount]"}
   * <p>
   * <i>Response in case the player not found</i>
   * <p>
   * Status: 404<br />
   * Body: {"error": "Can't find player with username '[username]'"}
   * 
   * @param username a {@code String} representing the player's username
   * @return a {@code ResponseEntity} with the {@code 200} status and the body
   *         containing information about the player with the given username
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
   * Body: [{"id": "[id]", "username": "[username]", "bestAttemptsCount":
   * "[bestAttemptsCount]"}, ...]
   * <p>
   * <i>Response in case the players not found</i>
   * <p>
   * Status: 200<br />
   * Body: []
   * 
   * @return a {@code ResponseEntity} with the {@code 200} status and the body
   *         containing information about the players with the best result
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
   * Body: {"id": "null", "username": "[username]", "bestAttemptsCount":
   * "[bestAttemptsCount]"}
   * <p>
   * <i>Normal response</i>
   * <p>
   * Status: 201<br />
   * Body: {"id": "[id]", "username": "[username]", "bestAttemptsCount":
   * "[bestAttemptsCount]"}
   * <p>
   * <i>Response in case the player with the given username already exists</i>
   * <p>
   * Status: 400<br />
   * Body: {"error": "Duplicating username"}
   * 
   * @param player a {@code PlayerSummary} representing the player to be created
   * @return a {@code ResponseEntity} with the {@code 201} status and the body
   *         containing information about the new player
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
   * Updates the player with the given id.<br />
   * The player's id from the request body is ignored.
   * <p>
   * Serves the {@code PUT} requests for the {@code /players/{id}} endpoints.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * PUT /players/[id]<br />
   * Cookie: JSESSIONID=[JSESSIONID]<br />
   * X-CSRF-TOKEN: [CSRF token]<br />
   * Body: {"id": "null", "username": "[username]", "bestAttemptsCount":
   * "[bestAttemptsCount]"}
   * <p>
   * <i>Normal response</i>
   * <p>
   * Status: 204
   * <p>
   * <i>Response in case the player not found</i>
   * <p>
   * Status: 404<br />
   * Body: {"error": "Can't find player with id = [id]"}
   * 
   * @param id a {@code Long} representing the player's id
   * @param player a {@code PlayerSummary} representing the player details to be
   *        updated
   * @return a {@code ResponseEntity} with the {@code 204} status and the empty
   *         body
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
   * Patches the player with the given id.<br />
   * The player's id from the request body is ignored.<br />
   * <p>
   * Serves the {@code PATCH} requests for the {@code /players/{id}} endpoints.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * PATCH /players/[id]<br />
   * Cookie: JSESSIONID=[JSESSIONID]<br />
   * X-CSRF-TOKEN: [CSRF token]<br />
   * Body: {(optional) "username": "[username]", (optional) "bestAttemptsCount":
   * "[bestAttemptsCount]"}
   * <p>
   * <i>Normal response</i>
   * <p>
   * Status: 204
   * <p>
   * <i>Response in case the player not found</i>
   * <p>
   * Status: 404<br />
   * Body: {"error": "Can't find player with id = [id]"}
   * 
   * @param id a {@code Long} representing the player's id
   * @param player a {@code PlayerSummary} representing the player details to be
   *        patched
   * @return a {@code ResponseEntity} with the {@code 204} status and the empty
   *         body
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
   * Serves the {@code DELETE} requests for the {@code /players/{id}} endpoints.
   * <p>
   * <b>Usage example</b>
   * <p>
   * <i>Request</i>
   * <p>
   * DELETE /players/[id]<br />
   * Cookie: JSESSIONID=[JSESSIONID]<br />
   * X-CSRF-TOKEN: [CSRF token]
   * <p>
   * <i>Normal response</i>
   * <p>
   * Status: 204
   * 
   * @param id a {@code Long} representing the player's id
   * @return a {@code ResponseEntity} with the {@code 204} status and the empty
   *         body
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable Long id) {
    this.playerService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
