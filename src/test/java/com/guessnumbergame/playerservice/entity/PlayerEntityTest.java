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
  @DisplayName("equals(Object) - the same object")
  final void equals_sameObject() throws Exception {
    PlayerEntity player = new PlayerEntity();
    player.setId(1L);
    player.setUsername("username1");
    player.setBestAttemptsCount(1);
    assertEquals(player, player);
  }
  
  @Test
  @DisplayName("equals(Object) - the same id")
  final void equals_sameId() throws Exception {
    PlayerEntity player1 = new PlayerEntity();
    player1.setId(1L);
    player1.setUsername("username1");
    player1.setBestAttemptsCount(1);
    PlayerEntity player2 = new PlayerEntity();
    player2.setId(1L);
    player2.setUsername("username2");
    player2.setBestAttemptsCount(2);
    assertEquals(player1, player2);
  }

  @Test
  @DisplayName("equals(Object) - different id")
  final void equals_differentId() throws Exception {
    PlayerEntity player1 = new PlayerEntity();
    player1.setId(1L);
    player1.setUsername("username");
    player1.setBestAttemptsCount(1);
    PlayerEntity player2 = new PlayerEntity();
    player2.setId(2L);
    player2.setUsername("username");
    player2.setBestAttemptsCount(1);
    assertNotEquals(player1, player2);
  }
  
  @Test
  @DisplayName("equals(Object) - one of the ids is null")
  final void equals_oneOfIdsIsNull() throws Exception {
    PlayerEntity player1 = new PlayerEntity();
    player1.setUsername("username");
    player1.setBestAttemptsCount(1);
    PlayerEntity player2 = new PlayerEntity();
    player2.setId(1L);
    player2.setUsername("username");
    player2.setBestAttemptsCount(1);
    assertNotEquals(player1, player2);
  }
  
  @Test
  @DisplayName("equals(Object) - both ids are null")
  final void equals_bothIdsAreNull() throws Exception {
    PlayerEntity player1 = new PlayerEntity();
    player1.setUsername("username");
    player1.setBestAttemptsCount(1);
    PlayerEntity player2 = new PlayerEntity();
    player2.setUsername("username");
    player2.setBestAttemptsCount(1);
    assertNotEquals(player1, player2);
  }

  @Test
  @DisplayName("hashCode() - any other PlayerEntity")
  final void hashCode_anyOtherPlayerEntity() throws Exception {
    PlayerEntity player1 = new PlayerEntity();
    player1.setId(1L);
    player1.setUsername("username1");
    player1.setBestAttemptsCount(1);
    PlayerEntity player2 = new PlayerEntity();
    player2.setId(2L);
    player2.setUsername("username2");
    player2.setBestAttemptsCount(2);
    assertEquals(player1.hashCode(), player2.hashCode());
  }

}
