package com.spfwproject.quotes.services;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.interfaces.IUserRepository;
import com.spfwproject.quotes.repositories.UserRepository;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess;


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

	public UserEntity createUser( UserEntity user) throws URISyntaxException {
		final String methodName = "createUser";
		logger.info("Entered " + methodName);

		UserEntity createdUser = userDBAccess.save(user);

		logger.info("Exiting method " + methodName + ".");
		return createdUser;
	}

	public UserEntity updateUser(Long id, UserEntity user) throws URISyntaxException {
		final String methodName = "updateUser";
		logger.info("Entered " + methodName);

		UserEntity userToBeUpdated = userDBAccess.findById(id).orElseThrow(RuntimeException::new);
		userToBeUpdated.setName(user.getName());
		userToBeUpdated.setEmail(user.getEmail());
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
