package com.spfwproject.quotes.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.PrivilegeEntity;
import com.spfwproject.quotes.entities.RoleEntity;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.UserNotFoundException;
import com.spfwproject.quotes.interfaces.UserService;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.repositories.RoleRepository;
import com.spfwproject.quotes.repositories.UserRepository;

@Component
public class UserServiceImpl implements UserService {
	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final RoleRepository roleRepository;

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo) {
		this.userRepository = userRepo;
		this.roleRepository = roleRepo;
	}

	public List<UserEntity> getUsers() {
		final String methodName = "getUsers";
		logger.info("Entered " + methodName);

		List<UserEntity> allusers = userRepository.findAll();

		logger.info("Exiting method " + methodName + ".");
		return allusers;

	}

	public UserEntity getUser(Long id) {
		final String methodName = "getUser";
		logger.info("Entered " + methodName + ", retrieving user with id: " + id);

		UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

		logger.info("Exiting method " + methodName + ".");
		return user;
	}

	public UserEntity getUserByUsername(String username) throws UsernameNotFoundException {
		final String methodName = "getUser";
		logger.info("Entered " + methodName + ", attempting to retrieve user with username: " + username);

		UserEntity user = userRepository.findUserByUsername(username);

		if (user == null) {
			logger.info("Error occured in retrieving user.");
			throw new UsernameNotFoundException("Problem during authentication!");
		}
		
		logger.info("User succesfully retrieved.");
		if (user.getRole() == null) {
			logger.info("Error occured in retrieving user role.");
			throw new UsernameNotFoundException("Issue obtaining role during authentication!");
		}
		logger.info("User role succesfully retrieved.");

		logger.info("Exiting method " + methodName + ".");
		return user;

	}
	
	public Long getUserIdByUsername(String username) throws UsernameNotFoundException {
		final String methodName = "getUser";
		logger.info("Entered " + methodName + ", attempting to retrieve user id with username: " + username);

		UserEntity user = getUserByUsername(username);
		logger.info("User role succesfully retrieved.");
		Long id = user.getId();
		
		logger.info("Exiting method " + methodName + "with id " + id + ".");
		return user.getId();

	}

	public boolean doesUsernameAlreadyExist(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findUserByUsername(username);

		return user == null ? false : true;
	}

	public UserEntity createUser(UserEntity user) throws URISyntaxException {
		final String methodName = "createUser";
		logger.info("Entered " + methodName);

		RoleEntity userRole = roleRepository.findByName("USER");
		
		user.setRole(userRole);
		UserEntity createdUser = userRepository.save(user);
		logger.info("User created.");

		logger.info("Exiting method " + methodName + ", successfully created user.");
		return createdUser;
	}

	// NOTE: be sure that you are sending the isLocked from the original entity if not changed
	// otherwise, account locked can be unintentionally changed
	public UserEntity updateUser(UserEntity updaterUserEntity, boolean isAccountLockedUpdated) {
		final String methodName = "updateUser";
		logger.info("Entered " + methodName);
		final Long id = updaterUserEntity.getId();

		UserEntity currentUserEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		String password = currentUserEntity.getPassword();
		UserEntity updatedUserEntity = setUpdateUserDetails(updaterUserEntity, 
				currentUserEntity, isAccountLockedUpdated);
		
		if (!updatedUserEntity.getPassword().equals(password)) {
			updatedUserEntity.setPassword(password);
		}

		userRepository.save(updatedUserEntity);
		logger.info("User updated.");


		logger.info("Exiting method " + methodName + ".");
		return currentUserEntity;
	}

	private UserEntity setUpdateUserDetails(UserEntity updaterDetails, UserEntity currentEntity, 
			boolean isAccountLockedUpdated) {
		// TODO: opt for hashmap of updated vars?
		final String  password = updaterDetails.getPassword();
		final String city = updaterDetails.getCity();
		final String country = updaterDetails.getCountry();
		final boolean isAccountLocked = updaterDetails.isAccountLocked();
		
		/*
		// ensure is hashed password
		if (password != null && password.isBlank()) {
			if (!currentEntity.getPassword().equals(password)) {
				currentEntity.setPassword(password);
			}
		}
		*/
		
		if (city != null && city.isBlank()) {
			if (!currentEntity.getCity().equals(city)) {
				currentEntity.setCity(city);
			}
		}
		
		if (country != null && country.isBlank()) {
			if (!currentEntity.getCountry().equals(country)) {
				currentEntity.setCountry(country);
			}
		}
		
		if (isAccountLockedUpdated) {
			if (!currentEntity.isAccountLocked() == isAccountLocked) {
					currentEntity.setPassword(password);
			}
		}
		
		return currentEntity;
	}
	
	public UserEntity lockUserAccount(Long id) {
		final String methodName = "lockUserAccount";
		logger.info("Entered " + methodName);

		UserEntity currentUserEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));		
		currentUserEntity.setAccountLocked(true);
		userRepository.save(currentUserEntity);
		logger.info("User with id: " + id + " account is locked.");

		logger.info("Exiting method " + methodName + ".");
		return currentUserEntity;
	}
	
	public UserEntity changePassword(Long id, String hashedPassword) {
		final String methodName = "lockUserAccount";
		logger.info("Entered " + methodName);

		UserEntity currentUserEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));		
		currentUserEntity.setPassword(hashedPassword);
		userRepository.save(currentUserEntity);
		logger.info("User with id: " + id + " account has password changed.");

		logger.info("Exiting method " + methodName + ".");
		return currentUserEntity;
	}

	public boolean deleteUser(Long id) {
		final String methodName = "deleteUser";
		logger.info("Entered " + methodName);

		userRepository.deleteById(id);
		logger.info("User with id: " + id + " has now been deleted.");


		logger.info("Exiting method " + methodName + ".");
		return true;
	}

}
