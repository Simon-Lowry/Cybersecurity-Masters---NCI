package com.spfwproject.quotes.services;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.spfwproject.quotes.controllers.QuoteController;
import com.spfwproject.quotes.entities.QuoteDBO;
import com.spfwproject.quotes.repositories.QuoteRepository;
import com.spfwproject.quotes.repositories.QuoteRepository;

public class QuoteService {
	private final QuoteRepository quoteRepository;

	private Logger logger = LoggerFactory.getLogger(QuoteService.class);

	public QuoteService(QuoteRepository quoteRepo) {
		this.quoteRepository = quoteRepo;
	}

	public List<QuoteDBO> getQuote() {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName);

		List<QuoteDBO> allQuotes = quoteRepository.findAll();

		logger.info("Exiting method " + methodName + ".");
		return allQuotes;

	}

	public QuoteDBO getQuote(@PathVariable Long id) {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName + ", retrieving Quote with id: " + id);

		QuoteDBO quote = quoteRepository.findById(id).orElseThrow(RuntimeException::new);

		logger.info("Exiting method " + methodName + ".");
		return quote;
	}

	@PostMapping
	public QuoteDBO createQuote(@RequestBody QuoteDBO Quote) throws URISyntaxException {
		final String methodName = "createQuote";
		logger.info("Entered " + methodName);

		QuoteDBO createdQuote = quoteRepository.save(Quote);

		logger.info("Exiting method " + methodName + ".");
		return createdQuote;
	}

	public QuoteDBO updateQuote(@PathVariable Long id, @RequestBody QuoteDBO quote) {
		final String methodName = "updateQuote";
		logger.info("Entered " + methodName);

		QuoteDBO quoteToBeUpdated = quoteRepository.findById(id).orElseThrow(RuntimeException::new);
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
