package com.guessnumbergame.api.service;

import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guessnumbergame.api.dto.PlayerSummary;
import com.guessnumbergame.api.entity.PlayerEntity;
import com.guessnumbergame.api.exception.PlayerNotFoundException;
import com.guessnumbergame.api.exception.PlayerNotUpdatedException;
import com.guessnumbergame.api.exception.handler.PlayerResponseEntityExceptionHandler;
import com.guessnumbergame.api.mapper.PlayerMapper;
import com.guessnumbergame.api.repository.PlayerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Default {@code PlayerService} implementation.
 * <p>
 * {@link PlayerSummary} objects are getting from and saved to a
 * {@link PlayerRepository}.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see PlayerSummary
 * @see PlayerRepository
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultPlayerService implements PlayerService {

  private final PlayerRepository playerRepository;

  private final PlayerMapper playerMapper;

  @Override
  public List<PlayerSummary> getAll() {
    return this.playerRepository.findAllPlayerSummaries();
  }

  @Override
  public PlayerSummary getById(Long id) {
    return this.playerRepository.findPlayerSummaryById(id)
        .orElseThrow(() -> new PlayerNotFoundException(id));
  }

  @Override
  public PlayerSummary getByUsername(String username) {
    return this.playerRepository.findPlayerSummaryByUsername(username)
        .orElseThrow(() -> new PlayerNotFoundException(username));
  }

  @Override
  public List<PlayerSummary> getPlayersWithBestResult() {
    return this.playerRepository.findPlayerSummariesWithBestResult();
  }

  @Override
  public PlayerSummary create(PlayerSummary player) {
    PlayerEntity playerEntity = this.playerMapper.playerSummaryToPlayerEntity(player);
    PlayerEntity savedPlayerEntity = this.playerRepository.save(playerEntity);
    return this.playerMapper.playerEntityToPlayerSummary(savedPlayerEntity);
  }

  @PreAuthorize("hasRole('ADMIN') || #player.username == authentication.name")
  @Transactional
  @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 5,
      backoff = @Backoff(delay = 100L), recover = "recoverUpdate")
  @Override
  public void update(Long id, PlayerSummary player) {
    PlayerEntity playerEntity = this.playerRepository.findById(id)
        .orElseThrow(() -> new PlayerNotFoundException(id));
    log.trace("Loaded player from {}: {}", PlayerRepository.class.getSimpleName(), playerEntity);
    playerEntity.setUsername(player.getUsername());
    playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
    PlayerEntity savedplayerEntity = this.playerRepository.save(playerEntity);
    log.trace("Saved player into {}: {}", PlayerRepository.class.getSimpleName(),
        savedplayerEntity);
  }

  /**
   * @see PlayerResponseEntityExceptionHandler
   */
  @Recover
  private void recoverUpdate(ObjectOptimisticLockingFailureException e, Long id,
      PlayerSummary player) {
    throw new PlayerNotUpdatedException(id);
  }

  @PreAuthorize("hasRole('ADMIN') || #player.username == authentication.name")
  @Transactional
  @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 5,
      backoff = @Backoff(delay = 100L), recover = "recoverPatch")
  @Override
  public void patch(Long id, PlayerSummary player) {
    PlayerEntity playerEntity = this.playerRepository.findById(id)
        .orElseThrow(() -> new PlayerNotFoundException(id));
    if (player.getUsername() != null) {
      playerEntity.setUsername(player.getUsername());
    }
    if (player.getBestAttemptsCount() != null) {
      playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
    }
    this.playerRepository.save(playerEntity);
  }

  /**
   * @see PlayerResponseEntityExceptionHandler
   */
  @Recover
  private void recoverPatch(ObjectOptimisticLockingFailureException e, Long id,
      PlayerSummary player) {
    throw new PlayerNotUpdatedException(id);
  }

  @Override
  public void deleteById(Long id) {
    this.playerRepository.deleteById(id);
  }

}
