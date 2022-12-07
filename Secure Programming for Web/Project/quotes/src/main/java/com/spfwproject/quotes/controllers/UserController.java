package com.spfwproject.quotes.controllers;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.NonEntityOwnerAuthorisationException;
import com.spfwproject.quotes.exceptions.UserNotFoundException;
import com.spfwproject.quotes.interfaces.AuthenticationService;
import com.spfwproject.quotes.interfaces.AuthorisationService;
import com.spfwproject.quotes.interfaces.UserService;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.models.UserResponse;
import com.spfwproject.quotes.validators.UserDetailsValidator;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private final UserService userService;
	@Autowired
	private final AuthorisationService authorisationService;
	
	@Autowired
	private final AuthenticationService authenticationService;

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	public UserController(UserService userService, AuthorisationService authorisationService, AuthenticationService authenticationService) {
		this.userService = userService;
		this.authorisationService = authorisationService;
		this.authenticationService = authenticationService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id)
			throws NonEntityOwnerAuthorisationException {
		final String methodName = "getUser";
		logger.info("Entered " + methodName + ", retrieving user with id: " + id);

		// check logged in user is the user that the request is looking to retrieve
		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(id);

		UserEntity user = null;
		try {
			user = userService.getUser(id);
			UserResponse userResponse = user.convertUserEntityToUserResponse();

			logger.info("Exiting method " + methodName + ".");
			return ResponseEntity.ok(userResponse);
		} catch (UserNotFoundException ex) {
			logger.error("Exception: " + ex);
			return (ResponseEntity) ResponseEntity.badRequest();
		}
	}

	@PutMapping
	public ResponseEntity updateUser(@RequestBody UserDetailsRequest user)
			throws URISyntaxException, NonEntityOwnerAuthorisationException {
		final String methodName = "updateUser";
		logger.info("Entered " + methodName + " with user: " + user);

		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(user.getId());

		UserDetailsValidator validator = new UserDetailsValidator(user);
		validator.validate();
		if (validator.containsErrors()) {
			logger.error("Exiting method, exception bad request" + methodName + ".");
			return ResponseEntity.badRequest().body(validator.getListOfErrors());
		}
		UserEntity updaterUser = user.convertUserDetailsToUserEntity();
		UserEntity updatedUser = userService.updateUser(updaterUser, false);
		ResponseEntity response = ResponseEntity.ok(user);

		logger.info("Exiting method " + methodName + ".");
		return response;
	}

	@DeleteMapping
	public ResponseEntity deleteUser(@RequestBody UserDetailsRequest userDetails)
			throws NonEntityOwnerAuthorisationException {
		final String methodName = "deleteUser";
		Long userId = userDetails.getId();
		String enteredPassowrd = userDetails.getPassword();
		logger.info("Entered " + methodName + " with user: "  + userDetails);

		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(userDetails.getId());

		// User has entered password and password has to be validated
		UserEntity user = userService.getUserByUsername(userDetails.getUsername());
		boolean isExpectedPassword = authenticationService.isExpectedPassword(enteredPassowrd, user.getPassword());
		if (isExpectedPassword) {
			logger.info("Password matched, deleting user and user quotes");
			userService.deleteUser(userId);
			// TODO: delete quotes and roles
			logger.info("Deletion complete.");
			ResponseEntity response = ResponseEntity.ok().build();
			logger.info("Exiting method " + methodName + ".");
			return response;

		} else {
			throw new BadCredentialsException("Password entered is not correct.");
		}
	}
}
