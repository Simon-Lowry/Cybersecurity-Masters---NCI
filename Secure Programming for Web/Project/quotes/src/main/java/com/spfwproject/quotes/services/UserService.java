package com.spfwproject.quotes.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.spfwproject.quotes.controllers.UserController;
import com.spfwproject.quotes.entities.User;
import com.spfwproject.quotes.repositories.UserRepository;


@Component
public class UserService {
	 private final UserRepository userRepository;
	    
	 private Logger logger = LoggerFactory.getLogger(UserService.class);

	 public UserService(UserRepository userRepository) {
		 this.userRepository = userRepository;
	 }

	public List<User> getUsers() {
		final String methodName = "getUsers";
		logger.info("Entered " + methodName);

		List<User> allusers = userRepository.findAll();

		logger.info("Exiting method " + methodName + ".");
		return allusers;

	}

	public User getUser(@PathVariable Long id) {
		final String methodName = "getUser";
		logger.info("Entered " + methodName + ", retrieving user with id: " + id);

		User user = userRepository.findById(id).orElseThrow(RuntimeException::new);

		logger.info("Exiting method " + methodName + ".");
		return user;
	}

	@PostMapping
	public User createUser(@RequestBody User user) throws URISyntaxException {
		final String methodName = "createUser";
		logger.info("Entered " + methodName);

		User createdUser = userRepository.save(user);

		logger.info("Exiting method " + methodName + ".");
		return createdUser;
	}

	public User updateUser(@PathVariable Long id, @RequestBody User user) {
		final String methodName = "updateUser";
		logger.info("Entered " + methodName);

		User userToBeUpdated = userRepository.findById(id).orElseThrow(RuntimeException::new);
		userToBeUpdated.setName(user.getName());
		userToBeUpdated.setEmail(user.getEmail());
		userToBeUpdated = userRepository.save(user);

		logger.info("Exiting method " + methodName + ".");
		return userToBeUpdated;
	}

	public boolean deleteUser(@PathVariable Long id) {
		final String methodName = "deleteUser";
		logger.info("Entered " + methodName);

		userRepository.deleteById(id);

		logger.info("Exiting method " + methodName + ".");
		return true;
	}

}
