package com.guessnumbergame.playerservice.mapper;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.guessnumbergame.playerservice.Application;
import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.entity.PlayerEntity;

@SpringBootTest(classes = Application.class)
@Tag("mapper")
@DisplayName("PlayerMapper")
class PlayerMapperTest {

  @Autowired
  private PlayerMapper playerMapper;

  @Test
  @DisplayName("playerSummaryToPlayerEntity() - ignores id")
  final void test() {
    PlayerSummary playerSummary = new PlayerSummary(1L, "ivan", 8);
    PlayerEntity playerEntity = this.playerMapper.playerSummaryToPlayerEntity(playerSummary);
    assertNull(playerEntity.getId());
  }

}
