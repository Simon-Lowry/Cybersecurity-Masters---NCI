package com.spwproject.quotes.dbaccesslayer;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spfwproject.quotes.entities.LoginAttemptsEntity;
import com.spfwproject.quotes.repositories.LoginAttemptsRepository;

public class LoginAttemptsDBAccess {
	private Logger logger = LoggerFactory.getLogger(LoginAttemptsDBAccess.class);
	
	private final LoginAttemptsRepository loginAttemptsRepository;
	
	public LoginAttemptsDBAccess(LoginAttemptsRepository loginAttemptsRepository) {
		this.loginAttemptsRepository = loginAttemptsRepository;
	}

	public Optional<LoginAttemptsEntity> getAttemptsByUsername(String email) {
		return loginAttemptsRepository.findAttemptsByUsername(email);
	}
	
	public void deleteLoginAttempts(List<Integer> listOfLoginAttemptIds) {
		loginAttemptsRepository.deleteAllById(listOfLoginAttemptIds);
	}

}
