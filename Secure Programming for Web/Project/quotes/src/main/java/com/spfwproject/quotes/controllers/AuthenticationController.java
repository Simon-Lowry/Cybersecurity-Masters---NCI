package com.spfwproject.quotes.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.interfaces.JWTTokenService;
import com.spfwproject.quotes.models.LoginRequest;
import com.spfwproject.quotes.models.TokenResponse;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.models.UserResponse;
import com.spfwproject.quotes.services.AuthenticationServiceImpl;
import com.spfwproject.quotes.services.JWTTokenServiceImpl;
import com.spfwproject.quotes.services.UserServiceImpl;
import com.spfwproject.quotes.validators.UserDetailsValidator;

@RestController
@RequestMapping("/auth/")
public class AuthenticationController {
	private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private final AuthenticationServiceImpl authenticationService;

	@Autowired
	private final UserServiceImpl userService;

	@Autowired
	private final JWTTokenService jwtTokenService;

	public AuthenticationController(AuthenticationServiceImpl authService, UserServiceImpl userService,
			JWTTokenService jwtTokenService) {
		this.authenticationService = authService;
		this.userService = userService;
		this.jwtTokenService = jwtTokenService;
	}

	@PostMapping("signUp")
	public ResponseEntity<TokenResponse> signUp(@RequestBody UserDetailsRequest signupFormRequest)
			throws URISyntaxException {
		final String methodName = "signUp";
		logger.info("Entered " + methodName + " endpoint.");

		UserDetailsValidator signUpFormValidator = authenticationService.validateSignupForm(signupFormRequest);
		if (signUpFormValidator.containsErrors()) { // if form validation was a success, continue with user creation
			ResponseEntity response = ResponseEntity.badRequest().body(signUpFormValidator.getListOfErrors());
			logger.error("Login details validation returned errors, bad request 400 returned. ");

			return response;
		}

		String plaintextPassword = signupFormRequest.getPassword();
		UserEntity userToCreate = signupFormRequest.convertUserDetailsToUserEntity(
				authenticationService.generatePasswordWithBCrypt(plaintextPassword));

		UserEntity createdUser = userService.createUser(userToCreate);
		Long userId = createdUser.getId();

		// TODO: transaction id and return in response & increase logs with these details in them	
		try {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userToCreate,
					null, userToCreate.getAuthorities());
			TokenResponse tokenResponseBody = authenticationService.setResponseBodyAndSecurityContextWithAuthInfo(
					userId, authentication);
			ResponseEntity<TokenResponse> response = ResponseEntity.created(new URI("/signUp/" + userToCreate.getId()))
					.body(tokenResponseBody);
	
			logger.info("Signup succesful returning response with user details & jwt token");
			return response;
		} catch (BadCredentialsException ex) {
			logger.error("Bad Credentials exception: " + ex);

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	@PostMapping("login")
	public ResponseEntity<Object> login(@RequestBody UserDetailsRequest request) {
		final String methodName = "login";
		logger.info("Entered " + methodName + " endpoint.");
		final String username = request.getUsername();

		UserDetailsValidator validator = new UserDetailsValidator(request);
		validator.validateLoginDetails();
		if (validator.containsErrors()) { // if form validation was a success, continue with check
			logger.info("Login details validation returned errors, bad request 400 returned. ");

			return ResponseEntity.badRequest().body(validator.getListOfErrors());
		}

		try {
			Authentication authentication = authenticationService.authenticate(
					new UsernamePasswordAuthenticationToken(username, request.getPassword()));
			Long userId = userService.getUserIdByUsername(username);
			
			TokenResponse tokenResponseBody = authenticationService.setResponseBodyAndSecurityContextWithAuthInfo(
					userId, authentication);
			ResponseEntity<Object> response = ResponseEntity.ok().body(tokenResponseBody);
			logger.error("Login succesful, response contains the token.");
			return response;

		} catch (Exception ex) {
			logger.error("Exception occurred: " + ex);

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
		}

	}

	/*
	@PostMapping("logout")
	public ResponseEntity performLogout(HttpServletRequest request) {
		ResponseEntity response = null;
		HttpSession session= request.getSession(false);

		SecurityContextHolder.clearContext();
        session= request.getSession(false);
       if(session != null) {
           session.invalidate();
       }
       for(Cookie cookie : request.getCookies()) {
           cookie.setMaxAge(0);
       }

		return (ResponseEntity) response.ok();
	}
	*/

}
