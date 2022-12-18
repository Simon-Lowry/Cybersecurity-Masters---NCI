package com.spfwproject.quotes.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.spfwproject.quotes.services.SessionServiceImpl;
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
	
	@Autowired
	private final SessionServiceImpl sessionService;

	public AuthenticationController(AuthenticationServiceImpl authService, UserServiceImpl userService,
			JWTTokenService jwtTokenService, SessionServiceImpl sessionService) {
		this.authenticationService = authService;
		this.userService = userService;
		this.jwtTokenService = jwtTokenService;
		this.sessionService = sessionService;
	}

	@PostMapping("signUp")
	public ResponseEntity<TokenResponse> signUp(@RequestBody UserDetailsRequest signupFormRequest, HttpServletRequest request)
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
		

		try {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userToCreate,
					null, userToCreate.getAuthorities());
			TokenResponse tokenResponseBody = authenticationService.setResponseBodyAndSecurityContextWithAuthInfo(
					userId, authentication, request);
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
	public ResponseEntity<Object> login(@RequestBody UserDetailsRequest requestBody, HttpServletRequest request,
			HttpServletResponse responseHttp) {
		final String methodName = "login";
		logger.info("Entered " + methodName + " endpoint.");
		logger.info("request session: " + request.getSession().getId());
		final String username = requestBody.getUsername();
		
		UserDetailsValidator validator = new UserDetailsValidator(requestBody);
		validator.validateLoginDetails();
		if (validator.containsErrors()) { // if form validation was a success, continue with check
			logger.info("Login details validation returned errors, bad request 400 returned. ");

			return ResponseEntity.badRequest().body(validator.getListOfErrors());
		}

		try {
			Authentication authentication = authenticationService.authenticate(
					new UsernamePasswordAuthenticationToken(username, requestBody.getPassword()));
			Long userId = userService.getUserIdByUsername(username);
			
			TokenResponse tokenResponseBody = authenticationService.setResponseBodyAndSecurityContextWithAuthInfo(
					userId, authentication, request);
			
			ResponseEntity<Object> response = ResponseEntity.ok().body(tokenResponseBody);
			
			logger.info("request session after change: " + request.getSession().getId());
			logger.info("Login succesful, response contains the token.");
			return response;

		} catch (Exception ex) {
			logger.error("Exception occurred: " + ex);

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
		}

	}

	
	@PostMapping("logout")
	public ResponseEntity<Object> performLogout(HttpServletRequest request) throws ServletException {
		HttpSession session= request.getSession(false);
		
		SecurityContextHolder.clearContext();
        session= request.getSession(false);
       if(session != null) {
           session.invalidate();
       }
       
       if (request.getCookies() != null) {
	       for(Cookie cookie : request.getCookies()) {
	           cookie.setMaxAge(0);
	       } 
       }
       
       SecurityContextHolder.clearContext();
       request.logout();

       SecurityContextHolder.getContext().setAuthentication(null);    
       sessionService.invalidateUserSession(session);
       
	   String loggedOutMessage = "User is successfully logged out.";
	   logger.info(loggedOutMessage);

	   ResponseEntity<Object> reponse = ResponseEntity.ok().body(loggedOutMessage);
	   return  reponse;
	}


}
