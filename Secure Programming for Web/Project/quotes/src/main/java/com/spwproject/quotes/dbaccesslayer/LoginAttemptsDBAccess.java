package com.spwproject.quotes.dbaccesslayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spfwproject.quotes.repositories.LoginAttemptsRepository;

public class LoginAttemptsDBAccess {
	private Logger logger = LoggerFactory.getLogger(LoginAttemptsDBAccess.class);

	private final LoginAttemptsRepository loginAttemptsRepository;

	public LoginAttemptsDBAccess(LoginAttemptsRepository loginAttemptsRepository) {
		this.loginAttemptsRepository = loginAttemptsRepository;
	}

	/*
	 * 
	 * public Optional<LoginAttemptsEntity> getAttemptsByUsername(Long userId) {
	 * return loginAttemptsRepository.findAttemptsByUserId(userId); }
	 * 
	 * public void deleteLoginAttempts(List<Integer> listOfLoginAttemptIds) {
	 * loginAttemptsRepository.deleteAllById(listOfLoginAttemptIds); }
	 */
}
