package com.lukesv.guessnumbergame.playerapi.service;

import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lukesv.guessnumbergame.playerapi.dto.PlayerSummaryDto;
import com.lukesv.guessnumbergame.playerapi.entity.PlayerEntity;
import com.lukesv.guessnumbergame.playerapi.exception.PlayerNotFoundException;
import com.lukesv.guessnumbergame.playerapi.exception.PlayerNotUpdatedException;
import com.lukesv.guessnumbergame.playerapi.mapper.PlayerMapper;
import com.lukesv.guessnumbergame.playerapi.repository.PlayerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultPlayerService implements PlayerService {

	private final PlayerRepository playerRepository;
	private final PlayerMapper playerMapper;

	@Override
	public List<PlayerSummaryDto> getAll() {
		return playerRepository.findAllSummaries();
	}

	@Override
	public PlayerSummaryDto getById(Long id) {
		return playerRepository.findSummaryById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
	}

	@Override
	public PlayerSummaryDto create(PlayerSummaryDto playerDto) {
		PlayerEntity playerEntity = playerMapper.playerDtoToPlayerEntity(playerDto);
		PlayerEntity savedPlayerEntity = playerRepository.save(playerEntity);
		return playerMapper.playerEntityToPlayerDto(savedPlayerEntity);
	}

	@Transactional
	@Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 5,
			backoff = @Backoff(delay = 100L), recover = "recoverUpdateById")
	@Override
	public void updateById(Long id, PlayerSummaryDto playerDto) {
		PlayerEntity playerEntity = playerRepository.findById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
		playerEntity.setUsername(playerDto.getUsername());
		playerEntity.setMinAttemptsCount(playerDto.getMinAttemptsCount());
		playerRepository.save(playerEntity);
	}

	@Override
	public void deleteById(Long id) {
		playerRepository.deleteById(id);
	}

	@Recover
	private void recoverUpdateById(ObjectOptimisticLockingFailureException e, Long id,
			PlayerSummaryDto playerDto) {
		throw new PlayerNotUpdatedException(id);
	}

}
