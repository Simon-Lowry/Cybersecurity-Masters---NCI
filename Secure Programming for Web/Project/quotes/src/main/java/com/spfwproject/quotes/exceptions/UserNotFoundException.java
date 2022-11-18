package com.spfwproject.quotes.exceptions;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    public UserNotFoundException(Long id) {
        super("The user with id " + id + "was not found");

    }

}
