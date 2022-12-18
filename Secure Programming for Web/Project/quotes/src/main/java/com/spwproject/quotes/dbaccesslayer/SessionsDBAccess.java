package com.spwproject.quotes.dbaccesslayer;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.LoginAttemptsEntity;
import com.spfwproject.quotes.entities.SessionEntity;
import com.spfwproject.quotes.repositories.LoginAttemptsRepository;
import com.spfwproject.quotes.repositories.SessionRepository;

@Component
public class SessionsDBAccess {
	private Logger logger = LoggerFactory.getLogger(SessionsDBAccess.class);

	@Autowired
	private SessionRepository sessionRepository;
	  
	public SessionEntity getAttemptsByUsername(String sessionId) {
		  return sessionRepository.getSessionBySessionId(sessionId);
	}

}
