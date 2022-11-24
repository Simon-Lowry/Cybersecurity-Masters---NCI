package com.spfwproject.quotes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Privilege escalation not possible. User role can not be altered.")
public class PrivilegeEscalationException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public PrivilegeEscalationException() {
		super("Privilege escalation not possible. User role can not be altered.");
	}

}
