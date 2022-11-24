package com.spfwproject.quotes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Login attempts have exceeded limit. Account is now blocked.")
public class LoginAttemptsLimitReachedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	String errorMessage = "Login attempts have exceeded limit. Account is now blocked.";
		
	public LoginAttemptsLimitReachedException() {
		super ( "Login attempts have exceeded limit. Account is now blocked.");
	}
	
	public String toString() {
		return "Login attempts have exceeded limit. Account is now blocked.";
	}
	
}
