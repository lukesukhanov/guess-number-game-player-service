package com.lukesv.guessnumbergame.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lukesv.guessnumbergame.api.dto.PlayerSummary;
import com.lukesv.guessnumbergame.api.service.PlayerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/players", produces = "application/json")
@RequiredArgsConstructor
public class PlayerController {

	private final PlayerService playerService;

	@GetMapping
	public ResponseEntity<?> getAll() {
		List<PlayerSummary> players = playerService.getAll();
		return ResponseEntity.ok(players);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		PlayerSummary player = playerService.getById(id);
		return ResponseEntity.ok(player);
	}

	@GetMapping("/withBestResult")
	public ResponseEntity<?> getPlayerWithMinBestAttemptsCount() {
		return ResponseEntity.ok(playerService.getPlayersWithBestResult());
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody PlayerSummary player) {
		PlayerSummary savedPlayer = playerService.create(player);
		return ResponseEntity.created(URI.create("/players/" + savedPlayer.getId())).body(savedPlayer);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody PlayerSummary player) {
		playerService.update(id, player);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> patchById(@PathVariable Long id, @Valid @RequestBody PlayerSummary player) {
		playerService.patch(id, player);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		playerService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
