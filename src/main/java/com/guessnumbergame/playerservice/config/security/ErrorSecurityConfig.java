package com.guessnumbergame.playerservice.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the {@code /error} endpoint.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 */
@Configuration
public class ErrorSecurityConfig {

  @Bean
  @Order(5)
  SecurityFilterChain errorSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .securityMatcher("/error")
        .securityContext(securityContext -> securityContext
            .disable())
        .cors(cors -> cors
            .disable())
        .csrf(csrf -> csrf
            .disable())
        .logout(logout -> logout
            .disable())
        .anonymous(anonymous -> anonymous
            .disable())
        .sessionManagement(sessionManagement -> sessionManagement
            .disable())
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers("/error").permitAll())
        .build();
  }

}
