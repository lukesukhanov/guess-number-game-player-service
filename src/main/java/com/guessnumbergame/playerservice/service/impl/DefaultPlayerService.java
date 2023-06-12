package com.guessnumbergame.playerservice.service.impl;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.entity.PlayerEntity;
import com.guessnumbergame.playerservice.exception.PlayerNotFoundException;
import com.guessnumbergame.playerservice.exception.PlayerNotUpdatedException;
import com.guessnumbergame.playerservice.mapper.PlayerMapper;
import com.guessnumbergame.playerservice.repository.PlayerRepository;
import com.guessnumbergame.playerservice.service.PlayerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The default {@code PlayerService} implementation.
 * <p>
 * {@link PlayerSummary} objects are getting from and saved into the
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

  private final ApplicationContext applicationContext;

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

  @Transactional
  @Retryable(
      retryFor = ObjectOptimisticLockingFailureException.class,
      noRetryFor = {
          AuthenticationCredentialsNotFoundException.class,
          AccessDeniedException.class },
      notRecoverable = {
          AuthenticationCredentialsNotFoundException.class,
          AccessDeniedException.class },
      maxAttempts = 5,
      backoff = @Backoff(delay = 100L),
      recover = "recoverUpdate")
  @Override
  public void update(Long id, PlayerSummary player) {
    PlayerEntity playerEntity = this.playerRepository.findById(id)
        .orElseThrow(() -> new PlayerNotFoundException(id));
    log.trace("Loaded player from {}: {}", PlayerRepository.class.getSimpleName(), playerEntity);
    DefaultPlayerService thisPlayerService = this.applicationContext.getBean(this.getClass());
    thisPlayerService.doUpdate(player, playerEntity);
  }

  @PreAuthorize("hasRole('ADMIN') || #playerEntity.username == authentication.name")
  void doUpdate(PlayerSummary player, PlayerEntity playerEntity) {
    playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
    PlayerEntity savedplayerEntity = this.playerRepository.save(playerEntity);
    log.trace("Saved player into {}: {}", PlayerRepository.class.getSimpleName(),
        savedplayerEntity);
  }

  @Recover
  private void recoverUpdate(RuntimeException e, Long id, PlayerSummary player) {
    throw new PlayerNotUpdatedException(id);
  }

  @Transactional
  @Retryable(
      retryFor = ObjectOptimisticLockingFailureException.class,
      noRetryFor = {
          AuthenticationCredentialsNotFoundException.class,
          AccessDeniedException.class },
      notRecoverable = {
          AuthenticationCredentialsNotFoundException.class,
          AccessDeniedException.class },
      maxAttempts = 5,
      backoff = @Backoff(delay = 100L),
      recover = "recoverPatch")
  @Override
  public void patch(Long id, PlayerSummary player) {
    PlayerEntity playerEntity = this.playerRepository.findById(id)
        .orElseThrow(() -> new PlayerNotFoundException(id));
    log.trace("Loaded player from {}: {}", PlayerRepository.class.getSimpleName(), playerEntity);
    DefaultPlayerService thisPlayerService = this.applicationContext.getBean(this.getClass());
    thisPlayerService.doPatch(player, playerEntity);
  }

  @PreAuthorize("hasRole('ADMIN') || #playerEntity.username == authentication.name")
  void doPatch(PlayerSummary player, PlayerEntity playerEntity) {
    if (player.getBestAttemptsCount() != null) {
      playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
    }
    PlayerEntity savedplayerEntity = this.playerRepository.save(playerEntity);
    log.trace("Saved player into {}: {}", PlayerRepository.class.getSimpleName(),
        savedplayerEntity);
  }

  @Recover
  private void recoverPatch(RuntimeException e, Long id, PlayerSummary player) {
    throw new PlayerNotUpdatedException(id);
  }

  @Override
  public void deleteById(Long id) {
    this.playerRepository.deleteById(id);
  }

}
