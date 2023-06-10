package com.guessnumbergame.playerservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.guessnumbergame.playerservice.dto.PlayerSummary;
import com.guessnumbergame.playerservice.entity.PlayerEntity;

import jakarta.persistence.LockModeType;

/**
 * The repository which allows to read and write information about players of
 * the game.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see PlayerEntity
 * @see PlayerSummary
 */
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long> {

  @Lock(LockModeType.OPTIMISTIC)
  Optional<PlayerEntity> findById(Long id);

  @Query(name = PlayerEntity.JPQL_FIND_ALL_PLAYER_SUMMARIES)
  List<PlayerSummary> findAllPlayerSummaries();

  @Query(name = PlayerEntity.JPQL_FIND_PLAYER_SUMMARY_BY_ID)
  Optional<PlayerSummary> findPlayerSummaryById(@Param("id") Long id);

  @Query(name = PlayerEntity.JPQL_FIND_PLAYER_SUMMARY_BY_USERNAME)
  Optional<PlayerSummary> findPlayerSummaryByUsername(@Param("username") String username);

  @Query(name = PlayerEntity.JPQL_FIND_PLAYER_SUMMARIES_WITH_BEST_RESULT)
  List<PlayerSummary> findPlayerSummariesWithBestResult();

  @Query(name = PlayerEntity.JPQL_DELETE_PLAYER_BY_ID)
  @Modifying
  void deleteById(@Param("id") Long id);

}
