package com.lukesv.guessnumbergame.api.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.lukesv.guessnumbergame.api.dto.User;
import com.lukesv.guessnumbergame.api.mapper.PlayerMapper;
import com.lukesv.guessnumbergame.api.service.PlayerService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.securityContext(securityContext -> securityContext.requireExplicitSave(true))
				.cors(cors -> cors.configurationSource(defaultCorsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.logout(logout -> logout
						.clearAuthentication(true)
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID"))
				.httpBasic(httpBasic -> httpBasic
						.authenticationEntryPoint(defaultAuthenticationEntryPoint()))
				.requestCache(cache -> cache.requestCache(defaultRequestCache())) // Избыточно
				.sessionManagement(sessionManagement -> sessionManagement
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						.requestMatchers("/error").permitAll()
						.requestMatchers(HttpMethod.GET).permitAll()
						.requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
						.requestMatchers(HttpMethod.PUT).hasRole("USER"))
				.build();
	}

	@Bean
	CorsConfigurationSource defaultCorsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	UserDetailsManager defaultUserDetailsManager(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}

	@Bean
	PasswordEncoder defaultPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationEntryPoint defaultAuthenticationEntryPoint() {
		return (request, response, e) -> {
			response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
		};
	}

	// Избыточно
	@Bean
	RequestCache defaultRequestCache() {
		HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
		requestCache.setMatchingRequestParameterName("continue");
		return requestCache;
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
