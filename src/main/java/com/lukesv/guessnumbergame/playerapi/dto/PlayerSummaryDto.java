package com.lukesv.guessnumbergame.playerapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "username")
@ToString
public class PlayerSummaryDto {

	private final Long id;

	@NotBlank(message = "Username is required")
	private final String username;

	@Positive(message = "Min attempts count must be positive")
	private final Integer minAttemptsCount;
}
