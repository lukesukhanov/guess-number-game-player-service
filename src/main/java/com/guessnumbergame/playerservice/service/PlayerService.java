package com.guessnumbergame.playerservice.service;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;

import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.exception.PlayerNotFoundException;
import com.guessnumbergame.playerservice.exception.PlayerNotUpdatedException;
import com.guessnumbergame.playerservice.exception.handler.PlayerResponseEntityExceptionHandler;

/**
 * The service managing players of the game.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see PlayerSummary
 */
public interface PlayerService {

  /**
   * Finds all existing players.
   * 
   * @return a {@code List} of all existing players
   */
  List<PlayerSummary> getAll();

  /**
   * Finds the player with the given id.
   * 
   * @param id a {@code Long} representing the player's id
   * @return a {@code PlayerSummary} with the given id
   * @throws PlayerNotFoundException if the player with the given id doesn't
   *         exist
   * @see PlayerResponseEntityExceptionHandler
   */
  PlayerSummary getById(Long id);

  /**
   * Finds the player with the given username.
   * 
   * @param username a {@code String} representing the player's username
   * @return a {@code PlayerSummary} with the given username
   * @throws PlayerNotFoundException if the player with the given username
   *         doesn't exist
   * @see PlayerResponseEntityExceptionHandler
   */
  PlayerSummary getByUsername(String username);

  /**
   * Finds the players with the best result.
   * 
   * @return a {@code List} of the players having the best result
   */
  List<PlayerSummary> getPlayersWithBestResult();

  /**
   * Creates the new player.
   * 
   * @param player a {@code PlayerSummary} representing the new player
   * @return saved {@code PlayerSummary}
   * @throws DuplicateKeyException if the player with the given username already
   *         exists
   * @see PlayerResponseEntityExceptionHandler
   */
  PlayerSummary create(PlayerSummary player);

  /**
   * Updates the player with the given id.
   * 
   * @param id a {@code Long} representing the player's id
   * @param player a {@code PlayerSummary} representing the player to be updated
   * @throws PlayerNotFoundException if the player with the given id doesn't
   *         exist
   * @throws PlayerNotUpdatedException if the player failed to be updated for
   *         technical reasons
   * @see PlayerResponseEntityExceptionHandler
   */
  void update(Long id, PlayerSummary player);

  /**
   * Patches the player with the given id.
   * 
   * @param id a {@code Long} representing the player's id
   * @param player a {@code PlayerSummary} representing the player details to be
   *        patched
   * @throws PlayerNotFoundException if the player with the given id doesn't
   *         exist
   * @throws PlayerNotUpdatedException if the player failed to be patched for
   *         technical reasons
   * @see PlayerResponseEntityExceptionHandler
   */
  void patch(Long id, PlayerSummary player);

  /**
   * Deletes the player with the given id.
   * 
   * @param id a {@code Long} representing the player's id
   */
  void deleteById(Long id);

}
