package com.spfwproject.quotes.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.constants.Roles;
import com.spfwproject.quotes.entities.PrivilegeEntity;
import com.spfwproject.quotes.entities.RoleEntity;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.UserNotFoundException;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.repositories.RoleRepository;
import com.spfwproject.quotes.repositories.UserRepository;
import com.spfwproject.quotes.validators.UserDetailsValidator;

@Component
public class UserService {
	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final RoleRepository roleRepository;

	private Logger logger = LoggerFactory.getLogger(UserService.class);

	public UserService(UserRepository userRepo, RoleRepository roleRepo) {
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
			throw new UsernameNotFoundException("Problem during authentication!");
		}

		logger.info("Exiting method " + methodName + ".");
		return user;

	}

	public boolean doesUsernameAlreadyExist(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findUserByUsername(username);

		return user == null ? false : true;
	}

	public UserEntity createUser(UserEntity user) throws URISyntaxException {
		final String methodName = "createUser";
		logger.info("Entered " + methodName);

		RoleEntity userRole = roleRepository.findByName("USER");
        user.setRoles(Arrays.asList(userRole));
		UserEntity createdUser = userRepository.save(user);

		logger.info("Exiting method " + methodName + ", successfully created user.");
		return createdUser;
	}

	public UserEntity updateUser(UserDetailsRequest userUpdateDetails) throws URISyntaxException {
		final String methodName = "updateUser";
		logger.info("Entered " + methodName);
		Long id = userUpdateDetails.getId();

    	// TODO: if update contians password, generate new hashed password and salt
		
		// TODO: use setUpdateUserDetails
		UserEntity currentUserEntity = userRepository.findById(id).orElseThrow(RuntimeException::new);
		UserEntity updatedUserEntity = setUpdateUserDetails(userUpdateDetails, currentUserEntity);
		userRepository.save(updatedUserEntity);

		logger.info("Exiting method " + methodName + ".");
		return updatedUserEntity;
	}
	
	private UserEntity setUpdateUserDetails(UserDetailsRequest updaterDetails, UserEntity currentEntity) {
		//TODO: finish this...
		return null;
	}

	public boolean deleteUser(Long id, String userPassword) {
		final String methodName = "deleteUser";
		logger.info("Entered " + methodName);

		userRepository.deleteById(id);

		logger.info("Exiting method " + methodName + ".");
		return true;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<RoleEntity> roles) {
		return getGrantedAuthorities(getPrivileges(roles));
	}

	private List<String> getPrivileges(Collection<RoleEntity> roles) {

		List<String> privileges = new ArrayList<>();
		List<PrivilegeEntity> collection = new ArrayList<>();
		for (RoleEntity role : roles) {
			privileges.add(role.getName());
			collection.addAll(role.getPrivileges());
		}
		for (PrivilegeEntity item : collection) {
			privileges.add(item.getName());
		}
		return privileges;
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}
}
