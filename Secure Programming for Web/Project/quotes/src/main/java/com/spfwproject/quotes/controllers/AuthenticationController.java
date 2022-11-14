package com.spfwproject.quotes.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.LoginRequest;
import com.spfwproject.quotes.models.SignUpFormRequest;
import com.spfwproject.quotes.models.UserResponse;
import com.spfwproject.quotes.services.AuthenticationService;
import com.spfwproject.quotes.services.UserService;
import com.spfwproject.quotes.validators.SignUpFormValidator;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private UserService userService;

	public AuthenticationController(AuthenticationService authService, UserService userService) {
		this.authenticationService = authService;
		this.userService = userService;
	}

	@PostMapping("/signUp")
	public ResponseEntity signUp(@RequestBody SignUpFormRequest signupFormRequest) throws URISyntaxException {
		final String methodName = "signUp";
		logger.info("Entered " + methodName);

		SignUpFormValidator signUpFormValidator = authenticationService.validateSignupForm(signupFormRequest);

		ResponseEntity response = null;
		if (!signUpFormValidator.containsErrors()) { // if form validation was a success, continue with user creation
			char[] passwordAsCharArray = signupFormRequest.getPassword().toCharArray();

			ArrayList<byte[]> passwordAndHash = authenticationService.generatePasswordHashWithSalt(passwordAsCharArray);
			logger.info("Password hash and salt generated");

			UserEntity userToCreate = signupFormRequest.convertSignUpFormToUserEntity(passwordAndHash.get(0).toString(),
					passwordAndHash.get(1).toString());

			userService.createUser(userToCreate);

			UserResponse userResponse = userToCreate.convertUserEntityToUserResponse();
			// TODO: generate session and return in response
			// TODO: generate token and return in response
			// TODO: transaction id and return in response & increase logs with these
			// details in them
			// TODO: XSRF with spring security
			logger.info("User created successfully, response data: " + userResponse.toString());

			UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(
					userResponse.getEmail(), userToCreate.getPassword());

			/*
			 * ServletRequestAttributes attr = (ServletRequestAttributes)
			 * RequestContextHolder.currentRequestAttributes(); HttpSession session =
			 * attr.getRequest().getSession(true); // true == allow create
			 * session.setAttribute("SPRING_SECURITY_CONTEXT", authReq);
			 */

			response = ResponseEntity.created(new URI("/signUp/" + userToCreate.getId())).body(userResponse);
		} else {
			response = ResponseEntity.badRequest().body(signUpFormValidator.getListOfErrors());
		}
		return response;
	}

	@PostMapping("/login")
	public ResponseEntity performLogin(@RequestBody LoginRequest loginRequest) {
		final String methodName = "performLogin";
		logger.info("Entered method: " + methodName);

		UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(), loginRequest.getPassword());

		authReq = (UsernamePasswordAuthenticationToken) authenticationService.authenticate(authReq);

		/*
		 * ServletRequestAttributes attr = (ServletRequestAttributes)
		 * RequestContextHolder.currentRequestAttributes(); HttpSession session =
		 * attr.getRequest().getSession(true); // true == allow create
		 * session.setAttribute("SPRING_SECURITY_CONTEXT", authReq);
		 */

		ResponseEntity response = ResponseEntity.ok(null);

		return response;
	}

	@PostMapping("/logout")
	public ResponseEntity performLogout(LoginRequest loginRequest) {
		ResponseEntity response = null;

		return response;
	}

}
