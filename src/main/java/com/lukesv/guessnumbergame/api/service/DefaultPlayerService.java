package com.lukesv.guessnumbergame.api.service;

import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
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

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public List<PlayerSummary> getAll() {
		return this.playerRepository.findAllPlayerSummaries();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public PlayerSummary getById(Long id) {
		return this.playerRepository.findPlayerSummaryById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
	}

	@PreAuthorize("hasRole('ADMIN') || #username == authentication.name")
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
		playerEntity.setUsername(player.getUsername());
		playerEntity.setBestAttemptsCount(player.getBestAttemptsCount());
		this.playerRepository.save(playerEntity);
	}

	@PreAuthorize("hasRole('ADMIN')")
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

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void deleteById(Long id) {
		this.playerRepository.deleteById(id);
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
