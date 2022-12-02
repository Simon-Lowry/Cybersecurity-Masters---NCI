package com.spfwproject.quotes.interfaces;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.UserDetailsRequest;

public interface UserService {
	public List<UserEntity> getUsers();

	public UserEntity getUser(Long id);

	public UserEntity getUserByUsername(String username) throws UsernameNotFoundException;

	public boolean doesUsernameAlreadyExist(String username);

	public UserEntity createUser(UserEntity user) throws URISyntaxException;

	public UserEntity updateUser(UserEntity updaterUserEntity, boolean isAccountLockedUpdated);

	public boolean deleteUser(Long id, String userPassword);
	
	public Long getUserIdByUsername(String username) throws UsernameNotFoundException;
}
