package com.spfwproject.quotes.services;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.spfwproject.quotes.controllers.QuoteController;
import com.spfwproject.quotes.entities.QuoteEntity;
import com.spfwproject.quotes.exceptions.QuoteNotFoundException;
import com.spfwproject.quotes.models.QuoteRequest;
import com.spfwproject.quotes.repositories.QuoteRepository;
import com.spfwproject.quotes.repositories.QuoteRepository;

@Component
public class QuoteService {
	private final QuoteRepository quoteRepository;

	private Logger logger = LoggerFactory.getLogger(QuoteService.class);

	public QuoteService(QuoteRepository quoteRepo) {
		this.quoteRepository = quoteRepo;
	}

	//TODO: make this into a get all quotes by a given user
	public List<QuoteEntity> getQuotes() {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName);

		List<QuoteEntity> allQuotes = quoteRepository.findAll();

		logger.info("Exiting method " + methodName + ".");
		return allQuotes;

	}

	public QuoteEntity getQuote( Long id) {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName + ", retrieving Quote with id: " + id);

		QuoteEntity quote = quoteRepository.findById(id).orElseThrow(() -> new QuoteNotFoundException(id));

		logger.info("Exiting method " + methodName + ".");
		return quote;
	}

	@PostMapping
	public QuoteEntity createQuote( QuoteRequest quoteRequest) throws URISyntaxException {
		final String methodName = "createQuote";
		logger.info("Entered " + methodName);

		QuoteEntity convertedQuoteEntity = QuoteEntity.convertQuoteRequestToQuoteEntity(quoteRequest);
		QuoteEntity createdQuote = quoteRepository.save(convertedQuoteEntity);

		logger.info("Exiting method " + methodName + ".");
		return createdQuote;
	}

	public QuoteEntity updateQuote( QuoteRequest quoteRequest) {
		final String methodName = "updateQuote";
		logger.info("Entered " + methodName);
		Long id = quoteRequest.getQuoteId();

		QuoteEntity quoteToBeUpdated = quoteRepository.findById(id).orElseThrow(() -> new QuoteNotFoundException(id));
		quoteToBeUpdated.setQuoteText(quoteRequest.getQuoteText());
		quoteToBeUpdated.setQuotePrivacySetting(quoteRequest.getQuotePrivacySetting());
		quoteToBeUpdated.setQuoteAuthor(quoteRequest.getQuotePrivacySetting());

		quoteToBeUpdated = quoteRepository.save(quoteToBeUpdated);

		logger.info("Exiting method " + methodName + ".");
		return quoteToBeUpdated;
	}

	public boolean deleteQuote( Long id) {
		final String methodName = "deleteQuote";
		logger.info("Entered " + methodName);

		quoteRepository.deleteById(id);

		logger.info("Exiting method " + methodName + ".");
		return true;
	}

}
