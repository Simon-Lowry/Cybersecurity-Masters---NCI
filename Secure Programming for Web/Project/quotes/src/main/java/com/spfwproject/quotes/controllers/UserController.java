package com.spfwproject.quotes.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spfwproject.quotes.repositories.UserRepository;
import com.spfwproject.quotes.models.User;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getUsers() {
    	final String methodName = "getUsers";
    	logger.info("Entered " + methodName);
    	
    	List<User> allusers =  userRepository.findAll();
    	
    	logger.info("Exiting method " + methodName + "." );
    	return allusers;
        
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
    	final String methodName = "getUser";
    	logger.info("Entered " + methodName + ", retrieving user with id: " + id);
    	
    	User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
    	 
    	logger.info("Exiting method " + methodName + "." );
        return user;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) throws URISyntaxException {
    	final String methodName = "createUser";
    	logger.info("Entered " + methodName);
        
    	User saveduser = userRepository.save(user);
        ResponseEntity response = ResponseEntity.created(new URI("/users/" + saveduser.getId())).body(saveduser);
        
        logger.info("Exiting method " + methodName + "." );
        return response;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody User user) {
    	final String methodName = "updateUser";
    	logger.info("Entered " + methodName);
       
    	User currentUser = userRepository.findById(id).orElseThrow(RuntimeException::new);
    	currentUser.setName(user.getName());
    	currentUser.setEmail(user.getEmail());
    	currentUser = userRepository.save(user);

        ResponseEntity response = ResponseEntity.ok(user);
       
        logger.info("Exiting method " + methodName + "." );
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
    	final String methodName = "deleteUser";
    	logger.info("Entered " + methodName);
        
    	userRepository.deleteById(id);
        ResponseEntity response = ResponseEntity.ok().build();
        
        logger.info("Exiting method " + methodName + "." );
        return response;
    }
}
