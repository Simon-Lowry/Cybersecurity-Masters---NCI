package com.spfwproject.quotes.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping
	public ResponseEntity getAllOfAUsersQuotes() {
		return (ResponseEntity) ResponseEntity.ok();
	}

	@GetMapping("/{id}")
	public ResponseEntity getQuote(@RequestBody QuoteRequest quoteRequest) throws NonEntityOwnerAuthorisationException {
		final String methodName = "getQuote";
		Long userId = quoteRequest.getUserId();
		Long quoteId = quoteRequest.getQuoteId();

		logger.info("Entered " + methodName + ", retrieving user with id: " + quoteId);
		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(userId);

		// TODO: is user making request authenticated
		// TODO: is user authorized to retrieve given user's information
		QuoteEntity quoteEntity = null;
		try {
			quoteEntity = quoteService.getQuoteById(quoteId);
			QuoteResponse quoteResponse = QuoteResponse.convertQuoteEntityToQuoteResponse(quoteEntity);

			logger.info("Exiting method " + methodName + ".");
			return ResponseEntity.ok(quoteResponse);
		} catch (QuoteNotFoundException ex) {
			logger.error("Exception: " + ex);
			return (ResponseEntity) ResponseEntity.badRequest();
		}
	}

	@PostMapping
	public ResponseEntity createQuote(@RequestBody QuoteRequest quoteRequest)
			throws NonEntityOwnerAuthorisationException {
		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(quoteRequest.getUserId());

		QuoteValidator quoteValidator = new QuoteValidator(quoteRequest);
		quoteValidator.validate();

		if (quoteValidator.containsErrors()) {
			return ResponseEntity.badRequest().body(quoteValidator.getListOfErrors());
		} else {
			try {
				quoteService.createQuote(quoteRequest);
				return (ResponseEntity) ResponseEntity.created(new URI("/quotes"));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				logger.info("Exception occured: " + e);
				return ResponseEntity.badRequest().body(e);
			}
		}
	}

	@PutMapping
	public ResponseEntity updateQuote(@RequestBody QuoteRequest quoteRequest)
			throws NonEntityOwnerAuthorisationException {
		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(quoteRequest.getUserId());

		QuoteValidator quoteValidator = new QuoteValidator(quoteRequest);
		quoteValidator.validate();

		if (quoteValidator.containsErrors()) {
			return ResponseEntity.badRequest().body(quoteValidator.getListOfErrors());
		} else {
			try {
				quoteService.updateQuote(quoteRequest);
				return (ResponseEntity) ResponseEntity.created(new URI("/quotes"));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				logger.info("Exception occured: " + e);
				return ResponseEntity.badRequest().body(e);
			}
		}
	}

	@DeleteMapping
	public ResponseEntity deleteQuote(@PathVariable Long id) throws NonEntityOwnerAuthorisationException {
		authorisationService.isAuthenticatedUserAuthorizedToActOnEntity(id);

		quoteService.deleteQuote(id);
		return ResponseEntity.ok(null);
	}
}
