package com.guessnumbergame.playerservice.config.security;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import com.guessnumbergame.playerservice.dto.User;
import com.guessnumbergame.playerservice.mapper.PlayerMapper;
import com.guessnumbergame.playerservice.service.PlayerService;

import lombok.extern.slf4j.Slf4j;

/**
 * General security settings.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 */
@Configuration
@Slf4j
public class GeneralSecurityConfig {

  @Bean
  UserDetailsManager defaultUserDetailsManager(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }

  @Bean
  PasswordEncoder defaultPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @ConditionalOnProperty("app.security.addAdminUser")
  CommandLineRunner addUsers(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder,
      PlayerService playerService, PlayerMapper playerMapper) {
    return args -> {
      User admin = new User("admin", passwordEncoder.encode("admin"), "ROLE_ADMIN", "ROLE_USER");
      userDetailsManager.createUser(admin);
      log.info("Created the user with admin authorities: {}", admin);
    };
  }

}
