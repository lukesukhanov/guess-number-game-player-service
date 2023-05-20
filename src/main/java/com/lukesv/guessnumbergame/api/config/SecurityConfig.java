package com.lukesv.guessnumbergame.api.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
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
						.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						.requestMatchers(HttpMethod.POST, "/register", "/error").permitAll()
						.requestMatchers(HttpMethod.POST).authenticated()
						.anyRequest().permitAll())
				.build();
	}

	@Bean
	SecurityContextRepository defaultSecurityContextRepository() {
		return new DelegatingSecurityContextRepository(
				new RequestAttributeSecurityContextRepository(),
				new HttpSessionSecurityContextRepository());
	}

	@Bean
	CorsConfigurationSource defaultCorsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
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

}
