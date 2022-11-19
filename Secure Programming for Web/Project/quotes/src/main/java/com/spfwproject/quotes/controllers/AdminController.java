package com.spfwproject.quotes.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.UserNotFoundException;
import com.spfwproject.quotes.models.LockUserRequest;
import com.spfwproject.quotes.models.UserResponse;
import com.spfwproject.quotes.services.AdminService;
import com.spfwproject.quotes.services.AuthenticationService;
import com.spfwproject.quotes.services.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private AdminService adminService;

	public AdminController(UserService userService, AdminService adminService) {
		this.userService = userService;
		this.adminService = adminService;
	}
	
	@GetMapping("/getUsers")
    public ResponseEntity<List<UserEntity>> getUsers() {
    	final String methodName = "getUsers";
    	logger.info("Entered " + methodName);
    	
    	List<UserEntity> allUsers =  userService.getUsers();	
        ResponseEntity<List<UserEntity>> response = ResponseEntity.ok(allUsers);
    	
    	logger.info("Exiting method " + methodName + "." );
    	return response;
        
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity getUser(@PathVariable Long id) {
    	final String methodName = "getUser";
    	logger.info("Entered " + methodName + ", retrieving user with id: " + id);
    	
    	UserEntity user = null;
    	try {
    		user = userService.getUser(id);   	
    		UserResponse userResponse = user.convertUserEntityToUserResponse();
    		
    		return ResponseEntity.ok(userResponse);
    	} catch (UserNotFoundException ex) {
    		logger.error("Exception: " + ex);
    		return (ResponseEntity) ResponseEntity.badRequest();
    	}
    }
    
    @PutMapping("/lockUser")
    public ResponseEntity<UserResponse> lockUser(@RequestBody LockUserRequest lockUserRequest) {
    	try {
    		UserEntity user = adminService.updateUserStatus(lockUserRequest);
    		UserResponse userResponse = user.convertUserEntityToUserResponse();

    		return ResponseEntity.ok(userResponse);
    	} catch (UserNotFoundException ex) {
    		logger.info("Exception " + ex);
    		return (ResponseEntity) ResponseEntity.badRequest().body(ex);

    	}
    }
	
}
