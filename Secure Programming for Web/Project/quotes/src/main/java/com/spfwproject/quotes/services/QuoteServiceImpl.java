package com.spfwproject.quotes.services;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.QuoteEntity;
import com.spfwproject.quotes.exceptions.QuoteNotFoundException;
import com.spfwproject.quotes.interfaces.QuoteService;
import com.spfwproject.quotes.models.QuoteRequest;
import com.spfwproject.quotes.repositories.QuoteRepository;

@Component
public class QuoteServiceImpl implements QuoteService {
	private final QuoteRepository quoteRepository;

	private Logger logger = LoggerFactory.getLogger(QuoteServiceImpl.class);

	public QuoteServiceImpl(QuoteRepository quoteRepo) {
		this.quoteRepository = quoteRepo;
	}

	public List<QuoteEntity> getQuotesForAUser(Long userId) {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName);

		List<QuoteEntity> allQuotes = quoteRepository.findAllQuotesForAGivenUser( userId);

		logger.info("Exiting method " + methodName + ".");
		return allQuotes;
	}

	public QuoteEntity getQuoteById(Long id) {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName + ", retrieving Quote with id: " + id);

		QuoteEntity quote = quoteRepository.findById(id).orElseThrow(() -> new QuoteNotFoundException(id));
		logger.info("Quote retrieved: " + quote);

		logger.info("Exiting method " + methodName + ".");
		return quote;
	}

	public QuoteEntity createQuote(QuoteRequest quoteRequest) throws URISyntaxException {
		final String methodName = "createQuote";
		logger.info("Entered " + methodName);

		QuoteEntity convertedQuoteEntity = QuoteEntity.convertQuoteRequestToQuoteEntity(quoteRequest);
		QuoteEntity createdQuote = quoteRepository.save(convertedQuoteEntity);

		logger.info("Exiting method " + methodName + ".");
		return createdQuote;
	}

	public QuoteEntity updateQuote(QuoteRequest quoteRequest) {
		final String methodName = "updateQuote";
		logger.info("Entered " + methodName + " with quote details: " + quoteRequest);
		Long id = quoteRequest.getQuoteId();

		QuoteEntity quoteToBeUpdated = quoteRepository.findById(id).orElseThrow(() -> new QuoteNotFoundException(id));
		logger.info("Original quote details retrieved from db: " + quoteToBeUpdated);

		quoteToBeUpdated.setQuoteText(quoteRequest.getQuoteText());
		logger.info("#2");

		quoteToBeUpdated.setQuotePrivacySetting(quoteRequest.getQuotePrivacySetting());
		logger.info("#3");

		quoteToBeUpdated.setQuoteAuthor(quoteRequest.getQuoteAuthor());
		logger.info("#4");


		quoteToBeUpdated = quoteRepository.save(quoteToBeUpdated);

		logger.info("Exiting method " + methodName + ". Quote successfully updated.");
		return quoteToBeUpdated;
	}

	public boolean deleteQuote(Long id) {
		final String methodName = "deleteQuote";
		logger.info("Entered " + methodName);

		quoteRepository.deleteById(id);

		logger.info("Exiting method " + methodName + ".");
		return true;
	}

}
