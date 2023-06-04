package com.guessnumbergame.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import com.guessnumbergame.api.dto.PlayerSummary;
import com.guessnumbergame.api.entity.PlayerEntity;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  PlayerEntity playerSummaryToPlayerEntity(PlayerSummary playerSummary);

  List<PlayerEntity> playerSummaryToPlayerEntity(List<PlayerSummary> playerSummaries);

  PlayerSummary playerEntityToPlayerSummary(PlayerEntity playerEntity);

  List<PlayerSummary> playerEntityToPlayerSummary(List<PlayerEntity> playerEntities);

}
