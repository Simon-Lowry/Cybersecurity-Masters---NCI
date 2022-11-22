package com.spfwproject.quotes.interfaces;

import java.net.URISyntaxException;
import java.util.List;

import com.spfwproject.quotes.entities.QuoteEntity;
import com.spfwproject.quotes.models.QuoteRequest;

public interface QuoteService {
	public boolean deleteQuote(Long id);

	public QuoteEntity updateQuote(QuoteRequest quoteRequest);

	public QuoteEntity createQuote(QuoteRequest quoteRequest) throws URISyntaxException;

	public QuoteEntity getQuoteById(Long id);

	public List<QuoteEntity> getQuotesForAUser();
}
