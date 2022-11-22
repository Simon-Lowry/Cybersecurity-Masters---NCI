package com.spfwproject.quotes.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Unauthorized access, only the entity owner can act on the entity")
public class NonEntityOwnerAuthorisationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NonEntityOwnerAuthorisationException(Long id) {
        super("Unauthorized access, only the entity owner can act on the entity with id: " + id);
    }
}
