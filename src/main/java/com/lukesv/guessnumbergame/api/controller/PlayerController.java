package com.lukesv.guessnumbergame.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lukesv.guessnumbergame.api.dto.PlayerSummary;
import com.lukesv.guessnumbergame.api.service.PlayerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/players", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PlayerController {

	private final PlayerService playerService;

	@GetMapping
	public ResponseEntity<?> getAll() {
		List<PlayerSummary> players = this.playerService.getAll();
		return ResponseEntity.ok(players);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		PlayerSummary player = this.playerService.getById(id);
		return ResponseEntity.ok(player);
	}

	@GetMapping(params = "username")
	public ResponseEntity<?> getByUsername(@RequestParam String username) {
		PlayerSummary player = this.playerService.getByUsername(username);
		return ResponseEntity.ok(player);
	}

	@GetMapping("/withBestResult")
	public ResponseEntity<?> getPlayerWithBestResult() {
		return ResponseEntity.ok(this.playerService.getPlayerWithBestResult());
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody PlayerSummary player) {
		PlayerSummary savedPlayer = this.playerService.create(player);
		return ResponseEntity.created(URI.create("/players/" + savedPlayer.getId())).body(savedPlayer);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody PlayerSummary player) {
		this.playerService.update(id, player);
		return ResponseEntity.noContent().build();
	}

	// @PatchMapping("/{id}")
	// public ResponseEntity<?> patch(@PathVariable Long id, @RequestBody PlayerSummary player) {
	// this.playerService.patch(id, player);
	// return ResponseEntity.noContent().build();
	// }
	//
	// @DeleteMapping("/{id}")
	// public ResponseEntity<?> deleteById(@PathVariable Long id) {
	// this.playerService.deleteById(id);
	// return ResponseEntity.noContent().build();
	// }

}
