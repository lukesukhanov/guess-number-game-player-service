package com.lukesv.guessnumbergame.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lukesv.guessnumbergame.api.dto.RegistrationForm;

@RestController
@RequestMapping(value = "/login", produces = "application/json")
public class LoginController {

	@PostMapping
	@ResponseStatus(code = HttpStatus.OK)
	public void login(RegistrationForm registrationForm) {
	}
}
