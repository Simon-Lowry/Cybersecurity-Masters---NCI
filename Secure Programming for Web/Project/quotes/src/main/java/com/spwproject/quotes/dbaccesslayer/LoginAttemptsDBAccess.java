package com.spwproject.quotes.dbaccesslayer;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.spfwproject.quotes.entities.LoginAttemptsEntity;

import com.spfwproject.quotes.repositories.LoginAttemptsRepository;

@Component
public class LoginAttemptsDBAccess {
	private Logger logger = LoggerFactory.getLogger(LoginAttemptsDBAccess.class);

	@Autowired
	private LoginAttemptsRepository loginAttemptsRepository;
	  
	public Optional<LoginAttemptsEntity> getAttemptsByUsername(String username) {
		  return loginAttemptsRepository.getAttemptsByUsername(username); 
	}
	
	public void saveAttemptsToDb(LoginAttemptsEntity loginAttempts) {
		loginAttemptsRepository.save(loginAttempts);
	}
	  
//	  public void deleteLoginAttempts(List<Integer> listOfLoginAttemptIds) {
//	  loginAttemptsRepository.deleteAllById(listOfLoginAttemptIds); }
	 
}
