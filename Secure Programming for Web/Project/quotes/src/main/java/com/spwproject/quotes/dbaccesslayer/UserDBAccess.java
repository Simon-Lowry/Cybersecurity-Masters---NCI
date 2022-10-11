package com.spwproject.quotes.dbaccesslayer;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.repositories.UserRepository;

public class UserDBAccess {
	private Logger logger = LoggerFactory.getLogger(UserDBAccess.class);

	private final UserRepository userRepository;

	public UserDBAccess(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserEntity> getAllUsers() {
		List<UserEntity> allUsers = userRepository.findAll();
		return allUsers;
	}

	public UserEntity getUser(@PathVariable Long id) {
		final String methodName = "getUser";
		logger.info("Entered " + methodName + ", retrieving user with id: " + id);

		UserEntity user = userRepository.findById(id).orElseThrow(RuntimeException::new);

		logger.info("Exiting method " + methodName + ".");
		return user;
	}

	public UserEntity createUser(@RequestBody UserEntity user) throws URISyntaxException {
		final String methodName = "createUser";
		logger.info("Entered " + methodName);

		UserEntity createdUser = userRepository.save(user);

		logger.info("Exiting method " + methodName + ".");
		return createdUser;
	}

	public UserEntity updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
		final String methodName = "updateUser";
		logger.info("Entered " + methodName);

		UserEntity userToBeUpdated = userRepository.findById(id).orElseThrow(RuntimeException::new);
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
