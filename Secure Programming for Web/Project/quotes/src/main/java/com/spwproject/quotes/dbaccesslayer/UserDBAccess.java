package com.spwproject.quotes.dbaccesslayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.repositories.UserRepository;

@Component
public class UserDBAccess implements UserDetailsService {
	private Logger logger = LoggerFactory.getLogger(UserDBAccess.class);

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findUserByUsername(username);

		if (user == null)
			throw new UsernameNotFoundException("User not found!");
			
		UserEntity userWithRoles = user.convertToUserEntityWithAuthorities();

		return userWithRoles;
	}

}
