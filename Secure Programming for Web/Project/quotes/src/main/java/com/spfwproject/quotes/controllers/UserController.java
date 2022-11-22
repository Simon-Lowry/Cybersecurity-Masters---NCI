package com.spfwproject.quotes.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.NonEntityOwnerAuthorisationException;
import com.spfwproject.quotes.exceptions.UserNotFoundException;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.models.UserResponse;
import com.spfwproject.quotes.services.AuthorisationService;
import com.spfwproject.quotes.services.UserService;
import com.spfwproject.quotes.validators.UserDetailsValidator;

@RestController
@RequestMapping("/users")
public class UserController {    
    @Autowired
    private final UserService userService;    
    @Autowired
    private AuthorisationService authorisationService;
    
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, AuthorisationService authorisationService) {
        this.userService = userService;
        this.authorisationService = authorisationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) throws NonEntityOwnerAuthorisationException {
    	final String methodName = "getUser";
    	logger.info("Entered " + methodName + ", retrieving user with id: " + id);
    	
    	authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(id);
    	
    	// TODO: is user authorized to retrieve given user's information    	
    	UserEntity user = null;
    	try {
    		user = userService.getUser(id);   	
    		UserResponse userResponse = user.convertUserEntityToUserResponse();
    		
        	logger.info("Exiting method " + methodName + "." );
    		return ResponseEntity.ok(userResponse);
    	} catch (UserNotFoundException ex) {
    		logger.error("Exception: " + ex);
    		return (ResponseEntity) ResponseEntity.badRequest();
    	}
    }

    @PutMapping
    public ResponseEntity updateUser(@RequestBody UserDetailsRequest user) throws URISyntaxException, NonEntityOwnerAuthorisationException {
    	final String methodName = "updateUser";
    	logger.info("Entered " + methodName);
    	
    	authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(user.getId());
       
		UserDetailsValidator validator = new UserDetailsValidator(user);
		validator.validate();
		if (validator.containsErrors()) {
	        logger.info("Exiting method, exception bad request" + methodName + "." );
			return ResponseEntity.badRequest().body(validator.getListOfErrors());
		}
    	UserEntity updatedUser = userService.updateUser(user);
        ResponseEntity response = ResponseEntity.ok(user);
       
        logger.info("Exiting method " + methodName + "." );
        return response;
    }

    @DeleteMapping
    public ResponseEntity deleteUser(@RequestBody UserDetailsRequest userDetails) throws NonEntityOwnerAuthorisationException {
    	final String methodName = "deleteUser";
    	logger.info("Entered " + methodName);
    	
    	authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(userDetails.getId());

    	//TODO: user is authenticated
    	//TODO: user has entered password and password has been validateds
    	//TODO: delete roles & quotes
        
    	userService.deleteUser(userDetails.getId(), userDetails.getPassword());
        ResponseEntity response = ResponseEntity.ok().build();
        
        logger.info("Exiting method " + methodName + "." );
        return response;
    }
}
