package com.guessnumbergame.playerservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.entity.PlayerEntity;

/**
 * The mappings between objects associated with players.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 */
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
