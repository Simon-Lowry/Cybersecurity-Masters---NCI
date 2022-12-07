package com.spfwproject.quotes.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.spfwproject.quotes.constants.QuotePrivacySettings;
import com.spfwproject.quotes.models.QuoteRequest;

public class QuoteValidator extends Validator{
	@Autowired
	QuoteRequest quoteRequest;
	
	public QuoteValidator(QuoteRequest quoteRequest) {
		this.quoteRequest = quoteRequest;
	}
	
	@Override
	public void validate() {
		if (quoteRequest.getUserId() == null) {
			addErrorMessageToErrorList("User Id must not be null");
		} 
		
		String quoteText = quoteRequest.getQuoteText();
		if (quoteText == null) {
			addErrorMessageToErrorList("Quote text must not be null");
		} else if (!isValidQuoteText(quoteText)) {
			addErrorMessageToErrorList("Quote text can only contain alphabetic, numeric or punctuation characters."
					+ " It must be no longer than 300 characters.");
		}
		
		String quoteAuthor = quoteRequest.getQuoteAuthor();
		if (quoteAuthor == null) {
			addErrorMessageToErrorList("Quote author must not be null");
		} else if (!isValidQuoteAuthor(quoteAuthor)) {
			addErrorMessageToErrorList("Quote author can only contain alphabetic or hyphen or apostrophe characters."
					+ " It must be no longer than 40 characters.");
		}
		
		String quotePrivacy = quoteRequest.getQuotePrivacySetting();
		if (quotePrivacy == null) {
			addErrorMessageToErrorList("Quote privacy setting must not be null");
		} else {
			try {
				QuotePrivacySettings.valueOf(quotePrivacy);
			} catch(Exception ex) {
				addErrorMessageToErrorList("Quote privacy setting must be either "
						+ "PUBLIC, PRIVATE or CONNECTION");
			}
		}
	}
	
	private boolean isValidQuoteText(String quoteText) {
		Pattern pattern = Pattern
				.compile("^(?=.*[0-9A-Za-z!-',?. ]).{1,300}$");
		Matcher matcher = pattern.matcher(quoteText);
		boolean isMatchFound = matcher.find();

		return isMatchFound;
	}
	
	private boolean isValidQuoteAuthor(String quoteAuthor) {
		Pattern pattern = Pattern
				.compile("^(?=.*[0-9A-Za-z'\\- ]).{1,40}$");
		Matcher matcher = pattern.matcher(quoteAuthor);
		boolean isMatchFound = matcher.find();

		return isMatchFound;
	}

}
