package com.lukesv.guessnumbergame.playerapi.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "GAME", name = "PLAYER")
@DynamicUpdate
@NamedQuery(name = "find_all_player_summaries",
		query = "select new com.lukesv.guessnumbergame.playerapi.dto.PlayerSummaryDto(id, username, minAttemptsCount) "
				+ "from PlayerEntity")
@NamedQuery(name = "find_player_summary_by_id",
		query = "select new com.lukesv.guessnumbergame.playerapi.dto.PlayerSummaryDto(id, username, minAttemptsCount) "
				+ "from PlayerEntity where id = :id")
@NamedQuery(name = "delete_player_by_id", query = "delete from PlayerEntity where id = :id")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "username")
public class PlayerEntity {

	public static final String JPQL_FIND_ALL_SUMMARIES = "find_all_player_summaries";
	public static final String JPQL_FIND_SUMMARY_BY_ID = "find_player_summary_by_id";
	public static final String JPQL_DELETE_BY_ID = "delete_player_by_id";

	@Id
	@SequenceGenerator(name = "game.common_id_seq", schema = "GAME", sequenceName = "COMMON_ID_SEQ",
			allocationSize = 5)
	@GeneratedValue(generator = "game.common_id_seq")
	@Column(name = "ID", updatable = false)
	protected Long id;

	@NotBlank(message = "Username is required")
	@Column(name = "USERNAME")
	protected String username;

	@Positive(message = "Min. attempts count must be positive")
	@Column(name = "MIN_ATTEMPTS_COUNT")
	protected Integer minAttemptsCount;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", insertable = false, updatable = false)
	protected Instant created;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED", insertable = false, updatable = false)
	protected Instant lastModified;
	
	@Version
	@Column(name = "VERSION", insertable = false, updatable = false)
	protected Integer version;
}
