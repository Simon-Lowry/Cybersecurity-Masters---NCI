package com.spfwproject.quotes.exceptions;

public class QuoteNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    public QuoteNotFoundException(Long id) {
        super("The quote with id " + id + "was not found");
    }
}
