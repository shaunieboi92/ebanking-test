package com.ebanking.uaa.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebanking.common.repository.UserRepository;
import com.ebanking.uaa.payload.AccountCreationResponse;
import com.ebanking.uaa.payload.AccountResponse;
import com.ebanking.uaa.payload.AuthenticationRequest;
import com.ebanking.uaa.payload.CreateUserRequest;
import com.ebanking.uaa.payload.JwtResponse;
import com.ebanking.uaa.service.AccountService;
import com.ebanking.uaa.utility.JwtUtility;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtility jwtUtils;

	@Value("${jwt.cookieName}")
	private String cookieName;

	@Value("${jwt.issuer}")
	private String issuer;

	@Autowired
	private AccountService accountService;

	@GetMapping("/login")
	public boolean login() {
		return true;
	}

	

}