package com.guessnumbergame.playerservice.entity;

import org.hibernate.annotations.DynamicUpdate;

import com.guessnumbergame.playerservice.repository.PlayerRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A player of the game.<br />
 * Contains the most important information about player.<br />
 * <p>
 * The optimistic locking is supported.
 * <p>
 * The {@code equals} method should be used for comparisons.
 * The {@code PlayerEntity} objects are compared by {@code id}.
 * The {@code PlayerEntity} with {@code id = null} is equal only to itself.
 * <p>
 * The {@code hashCode} method always returns the same value.
 * <p>
 * This class is not immutable and should not be used concurrently.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see PlayerRepository
 */
@Entity
@Table(name = "PLAYER")
@DynamicUpdate
@NamedQuery(name = "find_all_player_summaries",
    query = "select new com.guessnumbergame.playerservice.dto.PlayerSummary(id, username, bestAttemptsCount) "
        + "from PlayerEntity")
@NamedQuery(name = "find_player_summary_by_id",
    query = "select new com.guessnumbergame.playerservice.dto.PlayerSummary(id, username, bestAttemptsCount) "
        + "from PlayerEntity where id = :id")
@NamedQuery(name = "find_player_summary_by_username",
    query = "select new com.guessnumbergame.playerservice.dto.PlayerSummary(id, username, bestAttemptsCount) "
        + "from PlayerEntity where username = :username")
@NamedQuery(name = "find_player_summaries_with_best_result",
    query = "select new com.guessnumbergame.playerservice.dto.PlayerSummary(id, username, bestAttemptsCount) "
        + "from PlayerEntity "
        + "where bestAttemptsCount = (select min(bestAttemptsCount) from PlayerEntity)")
@NamedQuery(name = "delete_player_by_id", query = "delete from PlayerEntity where id = :id")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlayerEntity {

  public static final String JPQL_FIND_ALL_PLAYER_SUMMARIES = "find_all_player_summaries";

  public static final String JPQL_FIND_PLAYER_SUMMARY_BY_ID = "find_player_summary_by_id";

  public static final String JPQL_FIND_PLAYER_SUMMARY_BY_USERNAME = "find_player_summary_by_username";

  public static final String JPQL_FIND_PLAYER_SUMMARIES_WITH_BEST_RESULT = "find_player_summaries_with_best_result";

  public static final String JPQL_DELETE_PLAYER_BY_ID = "delete_player_by_id";

  @Id
  @SequenceGenerator(name = "common_id_seq", sequenceName = "COMMON_ID_SEQ",
      allocationSize = 5)
  @GeneratedValue(generator = "common_id_seq")
  @Column(name = "ID", updatable = false)
  protected Long id;

  @NotBlank(message = "Username is required")
  @Column(name = "USERNAME")
  protected String username;

  @Positive(message = "Best attempts count must be positive")
  @Column(name = "BEST_ATTEMPTS_COUNT")
  protected Integer bestAttemptsCount;

  @Version
  @Column(name = "VERSION", insertable = false, updatable = false)
  protected Integer version;

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlayerEntity)) {
      return false;
    }
    PlayerEntity other = (PlayerEntity) o;
    return this.id != null && this.id.equals(other.id);
  }

}
