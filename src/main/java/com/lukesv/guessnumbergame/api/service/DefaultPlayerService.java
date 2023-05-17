package com.lukesv.guessnumbergame.api.service;

import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lukesv.guessnumbergame.api.dto.PlayerSummary;
import com.lukesv.guessnumbergame.api.entity.PlayerEntity;
import com.lukesv.guessnumbergame.api.exception.PlayerNotFoundException;
import com.lukesv.guessnumbergame.api.exception.PlayerNotUpdatedException;
import com.lukesv.guessnumbergame.api.mapper.PlayerMapper;
import com.lukesv.guessnumbergame.api.repository.PlayerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultPlayerService implements PlayerService {

	private final PlayerRepository playerRepository;
	private final PlayerMapper playerMapper;

	@Override
	public List<PlayerSummary> getAll() {
		return playerRepository.findAllPlayerSummaries();
	}

	@Override
	public PlayerSummary getById(Long id) {
		return playerRepository.findPlayerSummaryById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
	}

	@Override
	public List<PlayerSummary> getPlayersWithBestResult() {
		return playerRepository.findPlayerSummariesWithBestResult();
	}

	@Override
	public PlayerSummary create(PlayerSummary player) {
		PlayerEntity playerEntity = playerMapper.playerSummaryToPlayerEntity(player);
		PlayerEntity savedPlayerEntity = playerRepository.save(playerEntity);
		return playerMapper.playerEntityToPlayerSummary(savedPlayerEntity);
	}

	@Transactional
	@Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 5,
			backoff = @Backoff(delay = 100L), recover = "recoverUpdate")
	@Override
	public void update(Long id, PlayerSummary player) {
		PlayerEntity playerEntity = playerRepository.findById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
		playerEntity.setUsername(player.getUsername());
		playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
		playerRepository.save(playerEntity);
	}

	@Transactional
	@Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 5,
			backoff = @Backoff(delay = 100L), recover = "recoverPatch")
	@Override
	public void patch(Long id, PlayerSummary player) {
		PlayerEntity playerEntity = playerRepository.findById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
		if (player.getUsername() != null) {
			playerEntity.setUsername(player.getUsername());
		}
		if (player.getBestAttemptsCount() != null) {
			playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
		}
		playerRepository.save(playerEntity);
	}

	@Override
	public void deleteById(Long id) {
		playerRepository.deleteById(id);
	}

	@Recover
	private void recoverUpdate(ObjectOptimisticLockingFailureException e, Long id,
			PlayerSummary player) {
		throw new PlayerNotUpdatedException(id);
	}

	@Recover
	private void recoverPatch(ObjectOptimisticLockingFailureException e, Long id,
			PlayerSummary player) {
		throw new PlayerNotUpdatedException(id);
	}
}
