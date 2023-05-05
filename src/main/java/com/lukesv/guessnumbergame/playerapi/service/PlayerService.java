package com.lukesv.guessnumbergame.playerapi.service;

import java.util.List;

import com.lukesv.guessnumbergame.playerapi.dto.PlayerSummaryDto;

public interface PlayerService {

	public List<PlayerSummaryDto> getAll();

	public PlayerSummaryDto getById(Long id);

	public PlayerSummaryDto create(PlayerSummaryDto player);

	public void updateById(Long id, PlayerSummaryDto player);

	public void deleteById(Long id);
}
