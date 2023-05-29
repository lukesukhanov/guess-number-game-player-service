package com.lukesv.guessnumbergame.api.config.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CsrfTokenSecurityConfig {

	@Bean
	SecurityFilterChain csrfTokenSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.securityMatcher("/csrfToken/**")
				.securityContext(securityContext -> securityContext
						.requireExplicitSave(true))
				.cors(cors -> cors
						.configurationSource(csrfTokenCorsConfigurationSource()))
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/csrfToken"))
				.logout(logout -> logout
						.disable())
				.httpBasic(httpBasic -> httpBasic
						.authenticationEntryPoint(this.csrfTokenAuthenticationEntryPoint()))
				.anonymous(anonymous -> anonymous
						.disable())
				.sessionManagement(sessionManagement -> sessionManagement
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						.requestMatchers(HttpMethod.POST, "/csrfToken").hasRole("USER"))
				.build();
	}

	@Bean
	CorsConfigurationSource csrfTokenCorsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
		configuration.setAllowedMethods(Arrays.asList("POST"));
		configuration.setExposedHeaders(Arrays.asList("X-CSRF-TOKEN"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/csrfToken", configuration);
		return source;
	}

	@Bean
	AuthenticationEntryPoint csrfTokenAuthenticationEntryPoint() {
		return (request, response, e) -> {
			response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
		};
	}
}
