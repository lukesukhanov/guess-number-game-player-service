package com.guessnumbergame.api.config.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class RegistrationSecurityConfig {

	@Bean
	SecurityFilterChain registrationSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.securityMatcher("/register/**")
				.securityContext(securityContext -> securityContext
						.disable())
				.cors(cors -> cors
						.configurationSource(registrationCorsConfigurationSource()))
				.csrf(csrf -> csrf
						.disable())
				.logout(logout -> logout
						.disable())
				.anonymous(anonymous -> anonymous
						.disable())
				.sessionManagement(sessionManagement -> sessionManagement
						.disable())
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						.requestMatchers(HttpMethod.POST, "/register").permitAll())
				.build();
	}

	@Bean
	CorsConfigurationSource registrationCorsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
		configuration.setAllowedMethods(Arrays.asList("POST"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization"));
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/register", configuration);
		return source;
	}

}
