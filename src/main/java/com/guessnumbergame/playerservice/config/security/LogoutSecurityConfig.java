package com.guessnumbergame.playerservice.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

/**
 * Security configuration for the {@code /logout} endpoint.
 * 
 * @author Luke Sukhanov
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class LogoutSecurityConfig {

  @Value("${app.security.cors-origins}")
  private final String[] corsOrigins;

  @Bean
  @Order(4)
  SecurityFilterChain logoutSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .securityMatcher("/logout")
        .securityContext(securityContext -> securityContext
            .requireExplicitSave(true))
        .headers(headers -> headers
            .httpStrictTransportSecurity(hsts -> hsts
                .disable()))
        .cors(cors -> cors
            .configurationSource(logoutCorsConfigurationSource()))
        .csrf(withDefaults())
        .logout(logout -> logout
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .addLogoutHandler(clearSiteDataLogoutHandler())
            .logoutSuccessHandler(defaultLogoutSuccessHandler())
            .deleteCookies("JSESSIONID"))
        .httpBasic(httpBasic -> httpBasic
            .authenticationEntryPoint(logoutAuthenticationEntryPoint()))
        .anonymous(anonymous -> anonymous
            .disable())
        .sessionManagement(sessionManagement -> sessionManagement
            .disable())
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers(HttpMethod.POST, "/logout").hasRole("USER"))
        .build();
  }

  @Bean
  CorsConfigurationSource logoutCorsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(this.corsOrigins));
    configuration.setAllowedMethods(Arrays.asList("POST"));
    configuration.setAllowedHeaders(Arrays.asList("X-CSRF-TOKEN"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/logout", configuration);
    return source;
  }

  @Bean
  HeaderWriterLogoutHandler clearSiteDataLogoutHandler() {
    return new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.COOKIES));
  }

  @Bean
  LogoutSuccessHandler defaultLogoutSuccessHandler() {
    return new HttpStatusReturningLogoutSuccessHandler();
  }

  @Bean
  AuthenticationEntryPoint logoutAuthenticationEntryPoint() {
    return (request, response, e) -> {
      response.sendError(HttpStatus.UNAUTHORIZED.value(),
          HttpStatus.UNAUTHORIZED.getReasonPhrase());
    };
  }

}
