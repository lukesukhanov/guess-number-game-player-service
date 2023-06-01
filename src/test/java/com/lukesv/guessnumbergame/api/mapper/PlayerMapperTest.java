package com.lukesv.guessnumbergame.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.guessnumbergame.api.Application;
import com.guessnumbergame.api.dto.PlayerSummary;
import com.guessnumbergame.api.entity.PlayerEntity;
import com.guessnumbergame.api.mapper.PlayerMapper;

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
		PlayerEntity expectedPlayerEntity = new PlayerEntity();
		expectedPlayerEntity.setUsername("ivan");
		expectedPlayerEntity.setBestAttemptsCount(8);
		PlayerEntity playerEntity = this.playerMapper.playerSummaryToPlayerEntity(playerSummary);
		assertEquals(playerEntity, expectedPlayerEntity);
	}

}
