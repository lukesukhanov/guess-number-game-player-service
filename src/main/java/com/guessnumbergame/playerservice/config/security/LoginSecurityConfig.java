package com.guessnumbergame.playerservice.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.guessnumbergame.playerservice.controller.LoginController;

import lombok.RequiredArgsConstructor;

/**
 * Security configuration for the {@code /login} endpoint.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see LoginController
 */
@Configuration
@RequiredArgsConstructor
public class LoginSecurityConfig {

  @Value("${app.security.cors-origins}")
  private final String[] corsOrigins;

  @Bean
  @Order(2)
  SecurityFilterChain loginSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .securityMatcher("/login")
        .securityContext(securityContext -> securityContext
            .requireExplicitSave(true))
        .cors(cors -> cors
            .configurationSource(loginCorsConfigurationSource()))
        .csrf(csrf -> csrf
            .disable())
        .logout(logout -> logout
            .disable())
        .httpBasic(httpBasic -> httpBasic
            .authenticationEntryPoint(loginAuthenticationEntryPoint()))
        .anonymous(anonymous -> anonymous
            .disable())
        .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers(HttpMethod.POST, "/login").permitAll())
        .build();
  }

  @Bean
  CorsConfigurationSource loginCorsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(this.corsOrigins));
    configuration.setAllowedMethods(Arrays.asList("POST"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/login/**", configuration);
    return source;
  }

  @Bean
  AuthenticationEntryPoint loginAuthenticationEntryPoint() {
    return (request, response, e) -> {
      response.sendError(HttpStatus.UNAUTHORIZED.value(),
          HttpStatus.UNAUTHORIZED.getReasonPhrase());
    };
  }

}
