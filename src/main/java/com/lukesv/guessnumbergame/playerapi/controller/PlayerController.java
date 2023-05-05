package com.lukesv.guessnumbergame.playerapi.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lukesv.guessnumbergame.playerapi.dto.PlayerSummaryDto;
import com.lukesv.guessnumbergame.playerapi.service.PlayerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

	private final PlayerService playerService;

	@GetMapping
	public ResponseEntity<?> getAll() {
		List<PlayerSummaryDto> players = playerService.getAll();
		return ResponseEntity.ok(players);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		PlayerSummaryDto player = playerService.getById(id);
		return ResponseEntity.ok(player);
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody PlayerSummaryDto playerDto) {
		PlayerSummaryDto savedPlayer = playerService.create(playerDto);
		return ResponseEntity.created(URI.create("/players/" + savedPlayer.getId())).body(savedPlayer);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody PlayerSummaryDto playerDto) {
		playerService.updateById(id, playerDto);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		playerService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
