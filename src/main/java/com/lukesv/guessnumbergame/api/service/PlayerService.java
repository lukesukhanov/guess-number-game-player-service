package com.lukesv.guessnumbergame.api.service;

import java.util.List;

import com.lukesv.guessnumbergame.api.dto.PlayerSummary;

public interface PlayerService {

	List<PlayerSummary> getAll();

	PlayerSummary getById(Long id);

	PlayerSummary getByUsername(String username);

	PlayerSummary getPlayerWithBestResult();

	PlayerSummary create(PlayerSummary player);

	void update(Long id, PlayerSummary player);

	// void patch(Long id, PlayerSummary player);
	//
	// void deleteById(Long id);
}
