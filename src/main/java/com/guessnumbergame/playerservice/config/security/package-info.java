/**
 * Security configurations for the different endpoints.<br />
 * There's also a {@link GeneralSecurityConfig} class which contains general security
 * settings.
 * <p>
 * Security filter chains are checked in the following order:
 * <ul>
 *  <li>{@code playerSecurityFilterChain}</li>
 *  <li>{@code loginSecurityFilterChain}</li>
 *  <li>{@code csrfTokenSecurityFilterChain}</li>
 *  <li>{@code logoutSecurityFilterChain}</li>
 *  <li>{@code errorSecurityFilterChain}</li>
 *  <li>{@code registrationSecurityFilterChain}</li>
 * </ul>
 */
package com.guessnumbergame.playerservice.config.security;