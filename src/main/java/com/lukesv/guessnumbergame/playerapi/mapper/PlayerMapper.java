package com.lukesv.guessnumbergame.playerapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import com.lukesv.guessnumbergame.playerapi.dto.PlayerSummaryDto;
import com.lukesv.guessnumbergame.playerapi.entity.PlayerEntity;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "created", ignore = true)
	@Mapping(target = "lastModified", ignore = true)
	PlayerEntity playerDtoToPlayerEntity(PlayerSummaryDto playerDto);

	List<PlayerEntity> playerDtoToPlayerEntity(List<PlayerSummaryDto> playerDtos);

	PlayerSummaryDto playerEntityToPlayerDto(PlayerEntity playerEntity);

	List<PlayerSummaryDto> playerEntityToPlayerDto(List<PlayerEntity> playerEntities);
}
