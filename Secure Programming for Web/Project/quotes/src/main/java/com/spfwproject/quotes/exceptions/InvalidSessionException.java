package com.spfwproject.quotes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Invalid session, rejecting request.")
public class InvalidSessionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidSessionException()  {
        super("Invalid session, rejecting request.");
    }
}
