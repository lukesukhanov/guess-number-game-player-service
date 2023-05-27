package com.lukesv.guessnumbergame.api.config;

import static org.springframework.security.config.Customizer.withDefaults;

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

@Configuration
public class PlayerSecurityConfig {
	
	@Bean
	SecurityFilterChain playerSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.securityMatcher("/players/**")
				.securityContext(securityContext -> securityContext
						.requireExplicitSave(true))
				.cors(cors -> cors
						.configurationSource(playerCorsConfigurationSource()))
				.csrf(withDefaults())
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
						.requestMatchers(HttpMethod.PUT, "/players/**").hasRole("USER"))
				.build();
	}

	@Bean
	CorsConfigurationSource playerCorsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT"));
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
			response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
		};
	}

}
