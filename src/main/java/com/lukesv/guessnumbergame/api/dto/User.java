package com.lukesv.guessnumbergame.api.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "username")
@ToString
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Username is required")
	protected String username;

	@NotEmpty(message = "Password is required")
	protected String password;

	@NotNull(message = "Property 'enabled' is required")
	protected Boolean enabled = true;

	protected Set<GrantedAuthority> authorities = new HashSet<>(4);

	public User(@NotBlank(message = "Username is required") String username,
			@NotEmpty(message = "Password is required") String password) {
		this.username = username;
		this.password = password;
		this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	}

	public User(@NotBlank(message = "Username is required") String username,
			@NotEmpty(message = "Password is required") String password, String... authorities) {
		this.username = username;
		this.password = password;
		for (String authority : authorities) {
			this.authorities.add(new SimpleGrantedAuthority(authority));
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.unmodifiableSet(this.authorities);
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
