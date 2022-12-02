package com.spfwproject.quotes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Malformed token, token is not formatted correctly")
public class MalformedTokenException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public MalformedTokenException()  {
        super("Malformed token, token is not formatted correctly");
    }
}