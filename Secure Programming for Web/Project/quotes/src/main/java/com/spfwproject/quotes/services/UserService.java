package com.spfwproject.quotes.services;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.repositories.UserRepository;

@Component
public class UserService {
	@Autowired
	private final UserRepository userDBAccess;

	private Logger logger = LoggerFactory.getLogger(UserService.class);

	public UserService(UserRepository userDBAccess) {
		this.userDBAccess = userDBAccess;
	}

	public List<UserEntity> getUsers() {
		final String methodName = "getUsers";
		logger.info("Entered " + methodName);

		List<UserEntity> allusers = userDBAccess.findAll();

		logger.info("Exiting method " + methodName + ".");
		return allusers;

	}

	public UserEntity getUser(Long id) {
		final String methodName = "getUser";
		logger.info("Entered " + methodName + ", retrieving user with id: " + id);

		UserEntity user = userDBAccess.findById(id).orElseThrow(RuntimeException::new);

		logger.info("Exiting method " + methodName + ".");
		return user;
	}

	public UserEntity getUserByUsername(String username) throws UsernameNotFoundException {
		final String methodName = "getUser";
		logger.info("Entered " + methodName + ", attempting to retrieve user with username: " + username);

		UserEntity user = userDBAccess.findUserByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Problem during authentication!");
		}

		logger.info("Exiting method " + methodName + ".");
		return user;

	}

	public boolean doesUsernameAlreadyExist(String username) throws UsernameNotFoundException {
		UserEntity user = userDBAccess.findUserByUsername(username);

		return user == null ? false : true;
	}

	public UserEntity createUser(UserEntity user) throws URISyntaxException {
		final String methodName = "createUser";
		logger.info("Entered " + methodName);

		UserEntity createdUser = userDBAccess.save(user);

		logger.info("Exiting method " + methodName + ", successfully created user.");
		return createdUser;
	}

	public UserEntity updateUser(Long id, UserEntity user) throws URISyntaxException {
		final String methodName = "updateUser";
		logger.info("Entered " + methodName);

		UserEntity userToBeUpdated = userDBAccess.findById(id).orElseThrow(RuntimeException::new);
		userToBeUpdated.setName(user.getName());
		userToBeUpdated.setUsername(user.getUsername());
		userToBeUpdated = userDBAccess.save(user);

		logger.info("Exiting method " + methodName + ".");
		return userToBeUpdated;
	}

	public boolean deleteUser(Long id) {
		final String methodName = "deleteUser";
		logger.info("Entered " + methodName);

		userDBAccess.deleteById(id);

		logger.info("Exiting method " + methodName + ".");
		return true;
	}

}
