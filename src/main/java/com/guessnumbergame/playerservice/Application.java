package com.guessnumbergame.playerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * This application provides REST API for the 'Guess my number' game.
 * <p>
 * The game is simple - the player has to guess a number from 1 to 50 with
 * minimal count of attempts.<br />
 * Information about players can be saved for the future.<br />
 * Each player has the id, the username and the count of his best attempts.
 * <p>
 * The API allows user:
 * <ul>
 * <li>to register, to login and to logout using basic authentication;</li>
 * <li>to receive and to save information about himself;</li>
 * <li>to receive information about players with the best result.</li>
 * </ul>
 * 
 * @version 1.0
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableRetry
@EnableMethodSecurity
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
