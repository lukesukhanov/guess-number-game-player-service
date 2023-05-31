package com.lukesv.guessnumbergame.api.entity;

import org.hibernate.annotations.DynamicUpdate;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PLAYER")
@DynamicUpdate
@NamedQuery(name = "find_all_player_summaries",
		query = "select new com.lukesv.guessnumbergame.api.dto.PlayerSummary(id, username, bestAttemptsCount) "
				+ "from PlayerEntity")
@NamedQuery(name = "find_player_summary_by_id",
		query = "select new com.lukesv.guessnumbergame.api.dto.PlayerSummary(id, username, bestAttemptsCount) "
				+ "from PlayerEntity where id = :id")
@NamedQuery(name = "find_player_summary_by_username",
		query = "select new com.lukesv.guessnumbergame.api.dto.PlayerSummary(id, username, bestAttemptsCount) "
				+ "from PlayerEntity where username = :username")
@NamedQuery(name = "find_player_summaries_with_best_result",
		query = "select new com.lukesv.guessnumbergame.api.dto.PlayerSummary(id, username, bestAttemptsCount) "
				+ "from PlayerEntity where bestAttemptsCount = (select min(bestAttemptsCount) from PlayerEntity)")
// @NamedQuery(name = "delete_player_by_id", query = "delete from PlayerEntity where id = :id")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "username")
public class PlayerEntity {

	public static final String JPQL_FIND_ALL_PLAYER_SUMMARIES = "find_all_player_summaries";

	public static final String JPQL_FIND_PLAYER_SUMMARY_BY_ID = "find_player_summary_by_id";

	public static final String JPQL_FIND_PLAYER_SUMMARY_BY_USERNAME = "find_player_summary_by_username";

	public static final String JPQL_FIND_PLAYER_SUMMARIES_WITH_BEST_RESULT = "find_player_summaries_with_best_result";

	// public static final String JPQL_DELETE_PLAYER_BY_ID = "delete_player_by_id";

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

}
