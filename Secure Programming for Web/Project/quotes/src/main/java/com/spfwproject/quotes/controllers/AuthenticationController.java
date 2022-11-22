package com.spfwproject.quotes.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.spfwproject.quotes.models.LoginRequest;
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
	private final JWTTokenServiceImpl jwtTokenService;

	public AuthenticationController(AuthenticationServiceImpl authService, UserServiceImpl userService,
			JWTTokenServiceImpl jwtTokenService ) {
		this.authenticationService = authService;
		this.userService = userService;
		this.jwtTokenService = jwtTokenService;
	}

	@PostMapping("signUp")
	public ResponseEntity<UserResponse> signUp(@RequestBody UserDetailsRequest signupFormRequest) throws URISyntaxException {
		final String methodName = "signUp";
		logger.info("Entered " + methodName);

		UserDetailsValidator signUpFormValidator = authenticationService.validateSignupForm(signupFormRequest);

		
		if (!signUpFormValidator.containsErrors()) { // if form validation was a success, continue with user creation
			String plaintextPassword = signupFormRequest.getPassword();

		//	ArrayList<byte[]> passwordAndHash = authenticationService.generatePasswordHashWithSalt(passwordAsCharArray);
		//	logger.info("Password hash and salt generated");

			UserEntity userToCreate = signupFormRequest.convertSignUpFormToUserEntity(
					authenticationService.generatePasswordWithBCrypt(plaintextPassword), null
					);
			
			userService.createUser(userToCreate);

			UserResponse userResponse = userToCreate.convertUserEntityToUserResponse();
			// TODO: transaction id and return in response & increase logs with these
			// details in them
			logger.info("User created successfully, response data: " + userResponse.toString());
			
			 try {		            
		            UsernamePasswordAuthenticationToken
		            authentication = new UsernamePasswordAuthenticationToken(
		            		userToCreate, null,
		            		userToCreate.getAuthorities()
		            );
		            SecurityContextHolder.getContext().setAuthentication(authentication);

		            ResponseEntity response = ResponseEntity.created(new URI("/signUp/" + userToCreate.getId()))
							 .header(
					                    HttpHeaders.AUTHORIZATION,
					                    jwtTokenService.generateToken(authentication)
					                )
							.body(userResponse);

		            return response;
		        } catch (BadCredentialsException ex) {
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		        }

		} else {
			ResponseEntity response = ResponseEntity.badRequest().body(signUpFormValidator.getListOfErrors());
			return response;
		}
	}

	 @PostMapping("login")
	 public ResponseEntity<UserResponse> login(@RequestBody @Valid LoginRequest request) {
	        try {
	            Authentication authentication = authenticationService
	                .authenticate(
	                    new UsernamePasswordAuthenticationToken(
	                        request.getUsername(), request.getPassword()
	                    )
	                );
	            SecurityContextHolder.getContext().setAuthentication(authentication);

	            logger.info("Made it to response entity on login!");

	            ResponseEntity<UserResponse> response = ResponseEntity.ok()
	                .header(
	                    HttpHeaders.AUTHORIZATION,
	                    jwtTokenService.generateToken(authentication)
	                   
	                ).body(new UserResponse(null, null, null, null, null));
		        return response;

	        } catch (BadCredentialsException ex) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }
	}

	@PostMapping("logout")
	public ResponseEntity performLogout(LoginRequest loginRequest) {
		ResponseEntity response = null;


		return (ResponseEntity) response.ok();
	}

}
