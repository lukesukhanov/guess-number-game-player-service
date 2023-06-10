package com.guessnumbergame.playerservice.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.guessnumbergame.playerservice.Application;

@SpringBootTest(classes = Application.class)
@DisplayName("PlayerSummary")
@Tag("dto")
@Tag("player")
class PlayerSummaryTest {

  @Test
  @DisplayName("equals(Object) - matching objects")
  final void equals_matchingObjects() throws Exception {
    PlayerSummary player1 = new PlayerSummary(1L, "username", 1);
    PlayerSummary player2 = new PlayerSummary(2L, "username", 2);
    assertEquals(player1, player2);
  }

  @Test
  @DisplayName("equals(Object) - not matching objects")
  final void equals_notMatchingObjects() throws Exception {
    PlayerSummary player1 = new PlayerSummary(1L, "username1", 1);
    PlayerSummary player2 = new PlayerSummary(1L, "username2", 1);
    assertNotEquals(player1, player2);
  }

  @Test
  @DisplayName("hashCode(Object) - matching objects")
  final void hashCode_matchingObjects() throws Exception {
    PlayerSummary player1 = new PlayerSummary(1L, "username", 1);
    PlayerSummary player2 = new PlayerSummary(2L, "username", 2);
    assertEquals(player1.hashCode(), player2.hashCode());
  }

}
