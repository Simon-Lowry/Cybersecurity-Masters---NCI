package com.spfwproject.quotes.services;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.NonEntityOwnerAuthorisationException;
import com.spfwproject.quotes.interfaces.AuthorisationService;

@Component
public class AuthorisationServiceImpl implements AuthorisationService {
	private Logger logger = LoggerFactory.getLogger(AuthorisationServiceImpl.class);

	public void isAuthenticatedUserAuthorizedToActOnEntity(Long id) throws NonEntityOwnerAuthorisationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();

		if (!Objects.equals(id, authenticatedUser.getId())) {
			throw new NonEntityOwnerAuthorisationException(id);
		}
	}

}
