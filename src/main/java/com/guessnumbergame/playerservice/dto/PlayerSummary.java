package com.guessnumbergame.playerservice.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A player of the game.<br />
 * Contains the most important information about player.
 * <p>
 * The {@code equals} method should be used for comparisons.
 * The {@code PlayerSummary} objects are compared by {@code id},
 * {@code username} and {@code bestAttemptsCount}.
 * <p>
 * This class is immutable and thread-safe.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = { "id", "username", "bestAttemptsCount" })
@ToString
public final class PlayerSummary implements Serializable {

  private static final long serialVersionUID = 1L;

  private final Long id;

  @NotBlank(message = "Username is required")
  private final String username;

  @Positive(message = "Best attempts count must be positive")
  private final Integer bestAttemptsCount;

}
