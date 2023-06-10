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

import com.guessnumbergame.playerservice.controller.PlayerController;

import lombok.RequiredArgsConstructor;

/**
 * Security configuration for the {@code /players/**} endpoints.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 * @see PlayerController
 */
@Configuration
@RequiredArgsConstructor
public class PlayerSecurityConfig {

  @Value("${app.security.cors-origins}")
  private final String[] corsOrigins;

  @Bean
  @Order(1)
  SecurityFilterChain playerSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .securityMatcher("/players/**")
        .securityContext(securityContext -> securityContext
            .requireExplicitSave(true))
        .headers(headers -> headers
            .httpStrictTransportSecurity(hsts -> hsts
                .disable()))
        .cors(cors -> cors
            .configurationSource(playerCorsConfigurationSource()))
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(request -> request.getMethod().equals("POST")))
        .logout(logout -> logout
            .disable())
        .httpBasic(httpBasic -> httpBasic
            .authenticationEntryPoint(playerAuthenticationEntryPoint()))
        .anonymous(anonymous -> anonymous
            .disable())
        .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers(HttpMethod.GET, "/players/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/players").permitAll()
            .requestMatchers(HttpMethod.PUT, "/players/**").hasRole("USER")
            .requestMatchers(HttpMethod.PATCH, "/players/**").hasRole("USER")
            .requestMatchers(HttpMethod.DELETE, "/players/**").hasRole("ADMIN"))
        .build();
  }

  @Bean
  CorsConfigurationSource playerCorsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(this.corsOrigins));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("Content-Type", "X-CSRF-TOKEN"));
    configuration.setExposedHeaders(Arrays.asList("Location"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/players/**", configuration);
    return source;
  }

  @Bean
  AuthenticationEntryPoint playerAuthenticationEntryPoint() {
    return (request, response, e) -> {
      response.sendError(HttpStatus.UNAUTHORIZED.value(),
          HttpStatus.UNAUTHORIZED.getReasonPhrase());
    };
  }

}
