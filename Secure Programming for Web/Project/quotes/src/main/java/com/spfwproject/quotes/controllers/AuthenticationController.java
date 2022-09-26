package com.spfwproject.quotes.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spfwproject.quotes.entities.User;
import com.spfwproject.quotes.services.AuthenticationService;
import com.spfwproject.quotes.services.UserService;

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
        
    @GetMapping("/signup")
    public ResponseEntity signUp() throws URISyntaxException {
    	User savedUser = new User(5L, "George Billiard", "something@gmail.com", null, null, false);
    
    	String password = "myPassword";
		char[] passwordAsCharArray = password.toCharArray();

    	ArrayList<byte[]> passwordAndHash = 
    			authenticationService.generatePasswordHashWithSalt(passwordAsCharArray);
    	
    	logger.info("done generating password");
    	savedUser.setHashedPassword(passwordAndHash.get(0));
    	savedUser.setSalt(passwordAndHash.get(1));

    	userService.createUser(savedUser);
    	
    	boolean isExpectedPassword = authenticationService.isExpectedPassword(
    			passwordAsCharArray, passwordAndHash.get(1), passwordAndHash.get(0));
    	logger.info("Expected password with legit password: " + isExpectedPassword);

    	
    	char[] fakePasswordArray = {'a', 'b', 'c', 'd', 'e'};  
    	isExpectedPassword =  authenticationService.isExpectedPassword(
    			fakePasswordArray, passwordAndHash.get(1), passwordAndHash.get(0));
    	logger.info("Expected password with NOT legit password: " + isExpectedPassword);

        ResponseEntity response = ResponseEntity.created(new URI("/signup/" + savedUser.getId())).body(savedUser);
        return response;
    }
    
}
