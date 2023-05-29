package com.lukesukhanov.guessnumbergame.playerapi.config.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.lukesv.guessnumbergame.api.dto.PlayerSummary;

@TestConfiguration
public class PlayersBuilder {

	@Bean
	List<PlayerSummary> players() {
		List<PlayerSummary> players = new ArrayList<>();
		players.add(new PlayerSummary(1l, "username1", 1));
		players.add(new PlayerSummary(2l, "username2", 2));
		players.add(new PlayerSummary(3l, "username3", 3));
		return Collections.unmodifiableList(players);
	}
}
