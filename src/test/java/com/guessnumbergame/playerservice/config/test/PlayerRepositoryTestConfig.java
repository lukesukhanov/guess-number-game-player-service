package com.guessnumbergame.playerservice.config.test;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class PlayerRepositoryTestConfig {

  private static final String SQL_INSERT_PLAYERS = """
      INSERT INTO player (username, best_attempts_count)
      VALUES
      	('ivan', 8),
      	('pyotr', 5),
      	('nadezhda', 7),
      	('boris', 5),
      	('darya', null);
      """;

  @Bean
  CommandLineRunner addPlayers(DataSource dataSource) {
    return args -> {
      try (Connection connection = dataSource.getConnection();
          Statement statement = connection.createStatement()) {
        statement.execute(SQL_INSERT_PLAYERS);
      }
    };
  }

}
