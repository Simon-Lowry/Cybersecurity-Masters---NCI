package com.spfwproject.quotes.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spfwproject.quotes.entities.QuoteEntity;
import com.spfwproject.quotes.exceptions.NonEntityOwnerAuthorisationException;
import com.spfwproject.quotes.exceptions.QuoteNotFoundException;
import com.spfwproject.quotes.interfaces.AuthorisationService;
import com.spfwproject.quotes.interfaces.QuoteService;
import com.spfwproject.quotes.models.QuoteRequest;
import com.spfwproject.quotes.models.QuoteResponse;
import com.spfwproject.quotes.models.TokenResponse;
import com.spfwproject.quotes.validators.QuoteValidator;

@RestController
@RequestMapping("/quotes")
public class QuoteController {
	@Autowired
	QuoteService quoteService;

	@Autowired
	AuthorisationService authorisationService;

	private Logger logger = LoggerFactory.getLogger(QuoteController.class);

	public QuoteController(QuoteService quoteService, AuthorisationService authorisationService) {
		this.quoteService = quoteService;
		this.authorisationService = authorisationService;
	}

	@GetMapping("/{userId}/{quoteId}")
	public ResponseEntity<QuoteResponse> getQuoteByQuoteId(@PathVariable("userId") Long userId, @PathVariable("quoteId") Long quoteId) throws NonEntityOwnerAuthorisationException {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName + ", retrieving user with id: " + quoteId);
		
		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(userId);

		QuoteEntity quoteEntity = null;
		try {
			quoteEntity = quoteService.getQuoteById(quoteId);
			QuoteResponse quoteResponse = QuoteResponse.convertQuoteEntityToQuoteResponse(quoteEntity);

			logger.info("Exiting method " + methodName + ", with quote response: " + quoteResponse);
			return ResponseEntity.ok(quoteResponse);
		} catch (QuoteNotFoundException ex) {
			logger.error("Exception: " + ex);
			return (ResponseEntity) ResponseEntity.badRequest();
		}
	}
	
	@GetMapping("/getAllQuotes/{userId}")
	public ResponseEntity getAllQuotesForAUser(@PathVariable("userId") Long userId) throws NonEntityOwnerAuthorisationException {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName + ", retrieving quotes with user id: " + userId);
		
		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(userId);

		List<QuoteEntity> quoteEntities = null;
		try {
			quoteEntities = quoteService.getQuotesForAUser(userId);

			logger.info("Exiting method " + methodName + ".");
			return ResponseEntity.ok(quoteEntities);
		} catch (QuoteNotFoundException ex) {
			logger.error("Exception: " + ex);
			return (ResponseEntity) ResponseEntity.badRequest();
		}
	}

	@PostMapping
	public ResponseEntity createQuote(@RequestBody QuoteRequest quoteRequest)
			throws NonEntityOwnerAuthorisationException {
		final String methodName = "createQuote";
		logger.info("Entered " + methodName + ", with quote details: " + quoteRequest);
		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(quoteRequest.getUserId());

		QuoteValidator quoteValidator = new QuoteValidator(quoteRequest);
		quoteValidator.validate();

		if (quoteValidator.containsErrors()) {
			logger.info("Exiting " + methodName + ", validation of quote details contains errors. Errors: " 
					+ quoteValidator.getListOfErrors());

			return ResponseEntity.badRequest().body(quoteValidator.getListOfErrors());
		} else {
			try {
				quoteService.createQuote(quoteRequest);
				logger.info("Exiting " + methodName + ", quote created");

				ResponseEntity response = new ResponseEntity(HttpStatus.CREATED);
				return response;
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				logger.info("Exception occured: " + e);
				return ResponseEntity.badRequest().body(e);
			}
		}
	}

	@PutMapping
	public ResponseEntity updateQuote(@RequestBody QuoteRequest quoteRequest) throws NonEntityOwnerAuthorisationException {
		final String methodName = "updateQuote";
		logger.info("Entered " + methodName + ", updating quote with these contents: " + quoteRequest);
		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(quoteRequest.getUserId());

		logger.info("Attempting to validate quote details....");
		QuoteValidator quoteValidator = new QuoteValidator(quoteRequest);
		quoteValidator.validate();
		logger.info("Validation complete");


		if (quoteValidator.containsErrors()) {
			logger.info("Validation contains errors: " + quoteValidator.getListOfErrors());

			return ResponseEntity.badRequest().body(quoteValidator.getListOfErrors());
		} else {
			try {
				logger.info("Attempting to update quote details... ");
				QuoteEntity quoteUpdated = quoteService.updateQuote(quoteRequest);
				logger.info("Exiting method. Quote updated.");

				return ResponseEntity.ok(quoteUpdated);
			} catch (Exception e) {
				logger.info("Exception occured: " + e);
				return ResponseEntity.badRequest().body(e);
			}
		}
	}

	@DeleteMapping
	public ResponseEntity<Object> deleteQuote(@RequestBody QuoteRequest quoteRequest) throws NonEntityOwnerAuthorisationException{
		final String methodName = "deleteQuote";
		logger.info("Entered " + methodName + ", deleting quote with these contents: " + quoteRequest);
		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(quoteRequest.getUserId());

		quoteService.deleteQuote(quoteRequest.getQuoteId());
		logger.info("Exiting " + methodName + ", deleted quote successfully.");

		return ResponseEntity.ok(null);
	}
}
