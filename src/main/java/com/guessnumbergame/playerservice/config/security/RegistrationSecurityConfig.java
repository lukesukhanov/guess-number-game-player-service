package com.guessnumbergame.playerservice.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.guessnumbergame.playerservice.controller.RegistrationController;

import lombok.RequiredArgsConstructor;

/**
 * Security configuration for the {@code /register} endpoint.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see RegistrationController
 */
@Configuration
@RequiredArgsConstructor
public class RegistrationSecurityConfig {

  @Value("${app.security.cors-origins}")
  private final String[] corsOrigins;

  @Bean
  @Order(6)
  SecurityFilterChain registrationSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .securityMatcher("/register")
        .securityContext(securityContext -> securityContext
            .disable())
        .headers(headers -> headers
            .httpStrictTransportSecurity(hsts -> hsts
                .disable()))
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
    configuration.setAllowedOrigins(Arrays.asList(this.corsOrigins));
    configuration.setAllowedMethods(Arrays.asList("POST"));
    configuration.setAllowedHeaders(Arrays.asList("Registration"));
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/register", configuration);
    return source;
  }

}
