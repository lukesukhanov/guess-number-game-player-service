package com.lukesv.guessnumbergame.api.service;

import java.util.List;

import com.lukesv.guessnumbergame.api.dto.PlayerSummary;

public interface PlayerService {

	public List<PlayerSummary> getAll();

	public PlayerSummary getById(Long id);

	public List<PlayerSummary> getPlayersWithBestResult();

	public PlayerSummary create(PlayerSummary player);

	public void update(Long id, PlayerSummary player);
	
	public void patch(Long id, PlayerSummary player);

	public void deleteById(Long id);
}
