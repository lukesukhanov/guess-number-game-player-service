package com.lukesv.guessnumbergame.playerapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lukesv.guessnumbergame.playerapi.dto.PlayerSummaryDto;
import com.lukesv.guessnumbergame.playerapi.entity.PlayerEntity;

import jakarta.persistence.LockModeType;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

	@Lock(LockModeType.OPTIMISTIC)
	List<PlayerEntity> findAll();

	@Lock(LockModeType.OPTIMISTIC)
	Optional<PlayerEntity> findById(Long id);

	@Query(name = PlayerEntity.JPQL_FIND_ALL_SUMMARIES)
	List<PlayerSummaryDto> findAllSummaries();

	@Query(name = PlayerEntity.JPQL_FIND_SUMMARY_BY_ID)
	Optional<PlayerSummaryDto> findSummaryById(@Param("id") Long id);

	@Query(name = PlayerEntity.JPQL_DELETE_BY_ID)
	@Modifying
	void deleteById(@Param("id") Long id);
}
