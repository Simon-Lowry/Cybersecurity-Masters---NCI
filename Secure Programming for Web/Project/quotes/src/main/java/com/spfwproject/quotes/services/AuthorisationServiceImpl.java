package com.spfwproject.quotes.services;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.QuoteEntity;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.NonEntityOwnerAuthorisationException;
import com.spfwproject.quotes.interfaces.AuthorisationService;
import com.spfwproject.quotes.interfaces.QuoteService;

@Component
public class AuthorisationServiceImpl implements AuthorisationService {
	private Logger logger = LoggerFactory.getLogger(AuthorisationServiceImpl.class);
	
	@Autowired
	QuoteService quoteService;

	public void isAuthenticatedUserAuthorizedToActOnEntity(Long id) throws NonEntityOwnerAuthorisationException {
		final String  methodName = "isAuthenticatedUserAuthorizedToActOnEntity";
		logger.info("Entered method: " + methodName);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();
		
		logger.info("Is user with id " + authenticatedUser.getId() + " able to act on entity with owner id "
				+ id);
		if (!Objects.equals(id, authenticatedUser.getId())) {
			logger.error("Error occurred, not matching user Ids");
			throw new NonEntityOwnerAuthorisationException(id);
		}
		logger.info("Authorization check complete: User is authorized to act on entity");
	}
	
	public void isAuthenticatedUserAuthorizedToActOnQuote(Long userId, Long quoteId) throws NonEntityOwnerAuthorisationException{
		final String  methodName = "isAuthenticatedUserAuthorizedToActOnQuote";
		logger.info("Entered method: " + methodName);

		QuoteEntity quoteEntity = quoteService.getQuoteById(quoteId);
		logger.info("Is user with id " + quoteEntity.getUserId() + " able to act on entity with owner id "
				+ userId);
		if (!Objects.equals(userId, quoteEntity.getUserId())) {
			logger.error("Error occurred, not matching user Ids");
			throw new NonEntityOwnerAuthorisationException(userId);
		}
		logger.info("Authorization check complete: User is authorized to act on entity");
		
	}


}
