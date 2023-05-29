package com.lukesv.guessnumbergame.api.config.security;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import com.lukesv.guessnumbergame.api.dto.User;
import com.lukesv.guessnumbergame.api.mapper.PlayerMapper;
import com.lukesv.guessnumbergame.api.service.PlayerService;

@Configuration
public class MainSecurityConfig {

	@Bean
	UserDetailsManager defaultUserDetailsManager(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}

	@Bean
	PasswordEncoder defaultPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner addUsers(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder,
			PlayerService playerService, PlayerMapper playerMapper) {
		return args -> {
			User admin = new User("admin", passwordEncoder.encode("admin"), "ROLE_ADMIN", "ROLE_USER");
			userDetailsManager.createUser(admin);
			User player = new User("vasya99", passwordEncoder.encode("vasya99"), "ROLE_USER");
			userDetailsManager.createUser(player);
		};
	}
}
