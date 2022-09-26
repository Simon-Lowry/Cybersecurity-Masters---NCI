package com.spfwproject.quotes.services;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.spfwproject.quotes.controllers.QuoteController;
import com.spfwproject.quotes.entities.Quote;
import com.spfwproject.quotes.repositories.QuoteRepository;
import com.spfwproject.quotes.repositories.QuoteRepository;

public class QuoteService {
	private final QuoteRepository quoteRepository;

	private Logger logger = LoggerFactory.getLogger(QuoteService.class);

	public QuoteService(QuoteRepository quoteRepo) {
		this.quoteRepository = quoteRepo;
	}

	public List<Quote> getQuote() {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName);

		List<Quote> allQuotes = quoteRepository.findAll();

		logger.info("Exiting method " + methodName + ".");
		return allQuotes;

	}

	public Quote getQuote(@PathVariable Long id) {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName + ", retrieving Quote with id: " + id);

		Quote quote = quoteRepository.findById(id).orElseThrow(RuntimeException::new);

		logger.info("Exiting method " + methodName + ".");
		return quote;
	}

	@PostMapping
	public Quote createQuote(@RequestBody Quote Quote) throws URISyntaxException {
		final String methodName = "createQuote";
		logger.info("Entered " + methodName);

		Quote createdQuote = quoteRepository.save(Quote);

		logger.info("Exiting method " + methodName + ".");
		return createdQuote;
	}

	public Quote updateQuote(@PathVariable Long id, @RequestBody Quote quote) {
		final String methodName = "updateQuote";
		logger.info("Entered " + methodName);

		Quote quoteToBeUpdated = quoteRepository.findById(id).orElseThrow(RuntimeException::new);
		quoteToBeUpdated.setQuoteText(quote.getQuoteText());
		quoteToBeUpdated.setQuotePrivacySetting(quote.getQuotePrivacySetting());
		quoteToBeUpdated = quoteRepository.save(quote);

		logger.info("Exiting method " + methodName + ".");
		return quoteToBeUpdated;
	}

	public boolean deleteQuote(@PathVariable Long id) {
		final String methodName = "deleteQuote";
		logger.info("Entered " + methodName);

		quoteRepository.deleteById(id);

		logger.info("Exiting method " + methodName + ".");
		return true;
	}

}
