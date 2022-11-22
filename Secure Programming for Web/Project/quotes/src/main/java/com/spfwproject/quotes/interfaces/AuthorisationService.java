package com.spfwproject.quotes.interfaces;

import com.spfwproject.quotes.exceptions.NonEntityOwnerAuthorisationException;

public interface AuthorisationService {
	public void isAuthenticatedUserAuthorizedToActOnEntity(Long id) throws NonEntityOwnerAuthorisationException;
}
