package com.lukesv.guessnumbergame.playerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableRetry
public class GuessNumberGamePlayerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuessNumberGamePlayerApiApplication.class, args);
	}

}
