package com.spfwproject.quotes.services;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.JWTAuthenticationFilter;
import com.spfwproject.quotes.constants.CookieNames;
import com.spfwproject.quotes.entities.LoginAttemptsEntity;
import com.spfwproject.quotes.entities.SessionEntity;
import com.spfwproject.quotes.exceptions.InvalidSessionException;
import com.spfwproject.quotes.repositories.SessionRepository;

@Component
public class SessionServiceImpl {
	private Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);	
	
	@Autowired
	private SessionRepository sessionRepository;
	
	
	private final Long sessionInactivityLength = 900000L;
	
	private final String UI_IP="0:0:0:0:0:0:0:1";
	
	public SessionServiceImpl(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}
	  
	public SessionEntity getSessionBySessionId(String sessionId) {
		  return sessionRepository.getSessionBySessionId(sessionId);
	}
	
	private Long getSessionTimeOutValue() {
		return sessionInactivityLength;
	}
	
	public void validateSession(HttpServletRequest request) {
		final String methodName = "validateSession";
		logger.info("Entered: " + methodName);

		String sessionId = getSessionIdFromCookie(request); 
		
		if (sessionId == null) {
			logger.info("Session not found for user, throwing exception" + methodName);
			throw new InvalidSessionException();
		}
		SessionEntity sessionEntity = getSessionBySessionId(sessionId);
		
		if (sessionEntity == null) {
			logger.info("Session not found in DB, throwing exception: " + methodName);
			if (!request.getRemoteAddr().equals(UI_IP)) {
				throw new InvalidSessionException();
			}
		} else {
			Long currentTime = System.currentTimeMillis();
			Long sessionCreationTime = sessionEntity.getSessionCreationTime();
			logger.info("Current time; " +  currentTime);
			logger.info("Session created time: " + sessionCreationTime);
			
			// is session expired? -> 900,000: 15 minutes of inactivity, session expired
			if (currentTime > (sessionCreationTime + sessionInactivityLength)) {
				throw new InvalidSessionException();
			} else { // otherwise, user inactivity finished, refresh user session
				sessionEntity.setSessionCreationTime(currentTime);
				sessionRepository.save(sessionEntity);	
			}
			
		}
		
		logger.info("Session validated, exiting method: " + methodName);
	}
	
	private String getSessionIdFromCookie(HttpServletRequest request) {
		if (request.getCookies() != null) {
			logger.info("pre check");
			for(Cookie cookie : request.getCookies()) {	 	           
	 	           if (cookie.getName() != null && cookie.getName().equals( "SESSION_ID")) {
		 	           logger.info("Cookie name: " + cookie.getName() + ", cookie value: " + cookie.getValue()
		 	        		   + ", domain: "+ cookie.getDomain());
	 	           }
	 	       } 
			logger.info("post check");

 	       for(Cookie cookie : request.getCookies()) {
 	           logger.info("Cookie name: " + cookie.getName() + ", cookie value: " + cookie.getValue());
 	           
 	           if (cookie.getName() != null && cookie.getName().equals(CookieNames.JSESSIONID.toString())) {
 	        	   return cookie.getValue();
 	           }
 	           // "SESSIONID"
 	       } 
		} 
		return null;
	}
	
	public void saveUserSession(HttpSession session, Long userId) {
		Optional<SessionEntity> optionalExistingSession = sessionRepository.findById(userId);
		
		SessionEntity existingSession = null;
		if (optionalExistingSession.isPresent()) {
			existingSession = optionalExistingSession.get();
			existingSession.setSessionId(session.getId());
			existingSession.setSessionCreationTime(session.getCreationTime());
			sessionRepository.save(existingSession);
		} else {
			SessionEntity newSessionEntity = new SessionEntity(userId, session.getId(), session.getCreationTime());
			sessionRepository.save(newSessionEntity);	
		}

	}
	
	public void invalidateUserSession(HttpSession session) {
		SessionEntity sessionEntity = sessionRepository.getSessionBySessionId(session.getId());
		
		if (sessionEntity != null) {
			Long invalidatedTime = System.currentTimeMillis() - sessionInactivityLength * 2;
			sessionEntity.setSessionCreationTime(invalidatedTime);
     		sessionRepository.save(sessionEntity);		
		}	
	}


}
