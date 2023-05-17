package com.lukesv.guessnumbergame.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lukesv.guessnumbergame.api.dto.PlayerSummary;
import com.lukesv.guessnumbergame.api.entity.PlayerEntity;

import jakarta.persistence.LockModeType;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

	@Lock(LockModeType.OPTIMISTIC)
	List<PlayerEntity> findAll();

	@Lock(LockModeType.OPTIMISTIC)
	Optional<PlayerEntity> findById(Long id);

	@Query(name = PlayerEntity.JPQL_FIND_ALL_PLAYER_SUMMARIES)
	List<PlayerSummary> findAllPlayerSummaries();

	@Query(name = PlayerEntity.JPQL_FIND_PLAYER_SUMMARY_BY_ID)
	Optional<PlayerSummary> findPlayerSummaryById(@Param("id") Long id);

	@Query(name = PlayerEntity.JPQL_FIND_PLAYER_SUMMARIES_WITH_BEST_RESULT)
	List<PlayerSummary> findPlayerSummariesWithBestResult();

	@Query(name = PlayerEntity.JPQL_DELETE_PLAYER_BY_ID)
	@Modifying
	void deleteById(@Param("id") Long id);
}
