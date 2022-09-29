package com.spfwproject.quotes.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spfwproject.quotes.entities.UserDBO;
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
    private final AuthenticationService authenticationService;  
    
    @Autowired
    private final UserService userService;    

    public AuthenticationController(AuthenticationService authService, UserService userService) {
        this.authenticationService = authService;
        this.userService = userService;
    }
        
    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody SignUpFormRequest signupForm) throws URISyntaxException {
    	SignUpFormValidator signUpFormValidator  = authenticationService.validateSignupForm(signupForm);
    	
		ResponseEntity response = null;
		if (!signUpFormValidator.containsErrors()) { // if form validation was a success, continue with user creation
			UserDBO userToCreate = signupForm.convertSignUpFormToUserEntity();
			
			char[] passwordAsCharArray = signupForm.getPassword().toCharArray();
	    	ArrayList<byte[]> passwordAndHash = authenticationService.generatePasswordHashWithSalt(passwordAsCharArray);
	    	
	    	userToCreate.setHashedPassword(passwordAndHash.get(0));
	    	userToCreate.setSalt(passwordAndHash.get(1));
	    	userService.createUser(userToCreate);
	
	    	UserResponse userResponse = userToCreate.convertUserEntityToUserResponse();
	    	// TODO: generate session and return in response
	    	// TODO: generate token and return in response
	    	// TODO: transaction id and return in response & increase logs with these details in them
	    	// TODO: XSRF with spring security
	    	logger.info("User created successfully, response data: " + userResponse.toString());
	        response = ResponseEntity.created(new URI("/signUp/" + userToCreate.getId())).body(userResponse);        
		} else {
			response = response.badRequest().body(signUpFormValidator.getListOfErrors());
		}
		return response;
    }
    
}
