package com.spfwproject.quotes.validators;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.utils.Utils;

public class UserDetailsValidator extends Validator {
	private Logger logger = LoggerFactory.getLogger(UserDetailsValidator.class);

	private UserDetailsRequest userDetails;

	public final static String PASSWORD_REPEAT_ERROR = "Passwords must match";
	public final static String PASSWORD_CONTENT_ERROR = "Password must contain at least one uppercase character, "
			+ "lower case character, special character, and be between 10 to 20 "
			+ "characters long. Apostrophes, hashes and hyphens are not allowed";
	public final static String USERNAME_NOT_SET_ERROR = "Username must contain a value";
	public final static String PASSWORD_NOT_SET_ERROR = "Password must contain a value";
	public final static String INVALID_CREDENTIALS_ERROR="Invalid credentials entered";


	public UserDetailsValidator(UserDetailsRequest userDetails) {
		this.userDetails = userDetails;
	}

	@Override
	public void validate() {
		logger.info("Beginning validation with user details: " + userDetails);
		String password = userDetails.getPassword();
		String passwordRepeat = userDetails.getPasswordRepeated();
		String username = userDetails.getUsername();
		String country = userDetails.getCountry();
		String city = userDetails.getCity();

		logger.info(userDetails.toString());

		if (formContainsNullOrEmptyEntries()) {
			return;
		}

		if (!password.equals(passwordRepeat)) {
			logger.info("Error: " +PASSWORD_REPEAT_ERROR );
			addErrorMessageToErrorList(PASSWORD_REPEAT_ERROR);
		}

		if (!validatePassword(password)) {
			logger.info("Error: " + PASSWORD_CONTENT_ERROR );
			addErrorMessageToErrorList(PASSWORD_CONTENT_ERROR);
		}

		if (!validateEmailAddress(username)) {
			logger.info("Error: Invalid username." );
			addErrorMessageToErrorList("Invalid username.");
		}

		if (!isInListOfAcceptedSignupCountries(country)) {
			logger.info("Error: Invalid country input." );
			addErrorMessageToErrorList("Invalid country input.");
		}

		if (!isValidCity(city)) {
			addErrorMessageToErrorList(
					"Invalid city. Cities can only have alphabetic characters, hyphens and apostrophes. First character of each word must be uppercase");
		}
		logger.info("Validation complete.");

	}	
	
	private boolean formContainsNullOrEmptyEntries() {
		logger.info("Checking for null or empty entries in user details.");
		if (Utils.isNullOrEmpty(userDetails.getName())) {
			addErrorMessageToErrorList("Name must contain a value");
		}

		if (Utils.isNullOrEmpty(userDetails.getCity())) {
			addErrorMessageToErrorList("City must contain a value");
		}

		if (Utils.isNullOrEmpty(userDetails.getCountry())) {
			addErrorMessageToErrorList("Country must contain a value");
		}

		if (Utils.isNullOrEmpty(userDetails.getUsername())) {
			addErrorMessageToErrorList("Username must contain a value");
		}

		if (Utils.isNullOrEmpty(userDetails.getPassword())) {
			addErrorMessageToErrorList("Password must contain a value");
		}

		if (Utils.isNullOrEmpty(userDetails.getPasswordRepeated())) {
			addErrorMessageToErrorList("Password repeated entry must contain a value");
		}

		logger.info("Complete checking for null or empty entries in user details.");
		return containsErrors();
	}
	
	public void validateLoginDetails() {
		logger.info("Validating login details.");

		if (Utils.isNullOrEmpty(userDetails.getUsername())) {
			addErrorMessageToErrorList(USERNAME_NOT_SET_ERROR);
			logger.info(USERNAME_NOT_SET_ERROR);
		}

		if (Utils.isNullOrEmpty(userDetails.getPassword())) {
			addErrorMessageToErrorList(PASSWORD_NOT_SET_ERROR);
			logger.info(PASSWORD_NOT_SET_ERROR);
			return;
		}
		
		if (!validateEmailAddress(userDetails.getUsername())) {
			logger.info("Error: Invalid username.");
			addErrorMessageToErrorList(INVALID_CREDENTIALS_ERROR);
			return;
		}

		logger.info("Complete validate login details.");
	}

	// Password must contain at least one uppercase character, lower case character,
	// special character, and be between 10 to 20 characters long
	private boolean validatePassword(String password) {
		logger.info("Validating password.");
		Pattern pattern = Pattern
				.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{10,20}$");
		Matcher matcher = pattern.matcher(password);
		boolean isMatchFound = matcher.find();

		logger.info("Validating password complete.");
		return isMatchFound;
	}

	// Local-part: uppercase and lowercase Latin letters A to Z and a to z, digits:
	// 0-9, allow dot, underscore and hyphen, dot not first or last char and not
	// consecutive dots, & max 64 characters Domain: same as above for letters 
	// and digits and dots, hyphen is not first or last character also.
	// Regex is from:
	// https://mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	private boolean validateEmailAddress(String emailAddress) {
		logger.info("Checking for valid email entry in user details.");

		Pattern pattern = Pattern.compile(
				"^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(emailAddress);
		boolean isMatchFound = matcher.find();

		logger.info("Complete checking for valid email entry in user details.");
		return isMatchFound;
	}

	// https://stackoverflow.com/questions/712231/best-way-to-get-a-list-of-countries-in-java
	private boolean isInListOfAcceptedSignupCountries(String country) {
		logger.info("Checking for valid country entry in user details.");

		String[] countryCodes = Locale.getISOCountries();

		for (String countryCode : countryCodes) {
			Locale locale = new Locale("", countryCode);

			if (country.equals(locale.getDisplayCountry())) {
				return true;
			}

		}

		logger.info("Complete checking for valid country entry in user details.");
		return false;
	}

	// covers alpha only, double barrel (Bora-Bora), spaced city names and city
	// names with apostrophes.
	// no longer than 20 characters, first character must be an uppercase letter, an
	// apostrophe is optional after first letter of any word,
	// followed by one or more alpha character
	private boolean isValidCity(String city) {
		logger.info("Checking for valid city entry in user details.");
		Pattern pattern = Pattern.compile("(?=.{2,20}$)^([A-Z])(')?([a-z]+)((\\-|\\s)[A-Z](')?[a-z]+)*$");
		Matcher matcher = pattern.matcher(city);
		boolean isMatchFound = matcher.find();

		logger.info("Complete checking for valid city entry in user details.");
		return isMatchFound;
	}

}
