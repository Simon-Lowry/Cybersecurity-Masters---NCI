package com.spfwproject.quotes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Invalid token, rejecting request.")
public class InvalidTokenException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidTokenException()  {
        super("Invalid token, rejecting request.");
    }
}