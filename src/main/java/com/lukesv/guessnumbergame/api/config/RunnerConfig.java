package com.lukesv.guessnumbergame.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import com.lukesv.guessnumbergame.api.dto.User;

@Configuration
public class RunnerConfig {

	@Bean
	CommandLineRunner addAdmin(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
		return args -> {
			User user = new User("admin", passwordEncoder.encode("admin"), "ROLE_ADMIN");
			userDetailsManager.createUser(user);
		};
	}
}
