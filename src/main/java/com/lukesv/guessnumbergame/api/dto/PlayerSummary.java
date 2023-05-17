package com.lukesv.guessnumbergame.api.dto;

import java.io.Serializable;

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
public final class PlayerSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Long id;

	@NotBlank(message = "Username is required")
	private final String username;

	@Positive(message = "Best attempts count must be positive")
	private final Integer bestAttemptsCount;

}
