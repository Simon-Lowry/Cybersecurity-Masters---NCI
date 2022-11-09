package com.spwproject.quotes.dbaccesslayer;

import java.net.URISyntaxException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.interfaces.IUserRepository;
import com.spfwproject.quotes.repositories.UserRepository;


@Repository
public class UserDBAccess implements IUserRepository{
	private Logger logger = LoggerFactory.getLogger(UserDBAccess.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	/*
	@Override
	public List<UserEntity> getAllUsers() {
		List<UserEntity> allUsers = userRepository.findAll();
		return allUsers;
	}
	

	@Override
	public UserEntity getUser(Long id) {
		final String methodName = "getUser";
		logger.info("Entered " + methodName + ", retrieving user with id: " + id);

		UserEntity user = userRepository.findById(id).orElseThrow(RuntimeException::new);

		logger.info("Exiting method " + methodName + ".");
		return user;
	}

	@Override
	public UserEntity createUser( UserEntity user) {
		final String methodName = "createUser";
		logger.info("Entered " + methodName);

		UserEntity createdUser = userRepository.save(user);

		logger.info("Exiting method " + methodName + ".");
		return createdUser;
	}

	@Override
	public UserEntity updateUser( Long id,  UserEntity user) {
		final String methodName = "updateUser";
		logger.info("Entered " + methodName);

		UserEntity userToBeUpdated = userRepository.findById(id).orElseThrow(RuntimeException::new);
		userToBeUpdated.setName(user.getName());
		userToBeUpdated.setEmail(user.getEmail());
		userToBeUpdated = userRepository.save(user);

		logger.info("Exiting method " + methodName + ".");
		return userToBeUpdated;
	}

	@Override
	public boolean deleteUser( Long id) {
		final String methodName = "deleteUser";
		logger.info("Entered " + methodName);

		userRepository.deleteById(id);

		logger.info("Exiting method " + methodName + ".");
		return true;
	}
	*/

}
