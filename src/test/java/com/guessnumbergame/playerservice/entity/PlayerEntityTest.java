package com.guessnumbergame.playerservice.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.guessnumbergame.playerservice.Application;

@SpringBootTest(classes = Application.class)
@DisplayName("PlayerEntity")
@Tag("entity")
@Tag("player")
class PlayerEntityTest {

  @Test
  @DisplayName("equals(Object) - matching objects")
  final void equals_matchingObjects() throws Exception {
    PlayerEntity player1 = new PlayerEntity();
    player1.setId(1L);
    player1.setUsername("username");
    player1.setBestAttemptsCount(1);
    PlayerEntity player2 = new PlayerEntity();
    player2.setId(2L);
    player2.setUsername("username");
    player2.setBestAttemptsCount(2);
    assertEquals(player1, player2);
  }

  @Test
  @DisplayName("equals(Object) - not matching objects")
  final void equals_notMatchingObjects() throws Exception {
    PlayerEntity player1 = new PlayerEntity();
    player1.setId(1L);
    player1.setUsername("username1");
    player1.setBestAttemptsCount(1);
    PlayerEntity player2 = new PlayerEntity();
    player2.setId(1L);
    player2.setUsername("username2");
    player2.setBestAttemptsCount(1);
    assertNotEquals(player1, player2);
  }

  @Test
  @DisplayName("hashCode() - matching objects")
  final void hashCode_matchingObjects() throws Exception {
    PlayerEntity player1 = new PlayerEntity();
    player1.setId(1L);
    player1.setUsername("username");
    player1.setBestAttemptsCount(1);
    PlayerEntity player2 = new PlayerEntity();
    player2.setId(2L);
    player2.setUsername("username");
    player2.setBestAttemptsCount(2);
    assertEquals(player1.hashCode(), player2.hashCode());
  }

}
