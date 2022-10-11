package com.spfwproject.quotes.validators;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.SignUpFormRequest;
import com.spfwproject.quotes.utils.Utils;

public class SignUpFormValidator extends Validator {
	private Logger logger = LoggerFactory.getLogger(SignUpFormValidator.class);

	private SignUpFormRequest signupForm;

	public SignUpFormValidator(SignUpFormRequest signupForm) {
		this.signupForm = signupForm;
	}

	@Override
	public void validate() {
		String password = signupForm.getPassword();
		String passwordRepeat = signupForm.getPasswordRepeated();
		String email = signupForm.getEmail();
		String country = signupForm.getCountry();
		String city = signupForm.getCity();
		
		logger.info(signupForm.toString());
		
		if (formContainsNullOrEmptyEntries()) {
			return;
		}

		if (!password.equals(passwordRepeat)) {
			addErrorMessageToErrorList("Passwords must match.");
		}

		if (!validatePassword(password)) {
			addErrorMessageToErrorList(
					"Password must contain at least one uppercase character, lower case character, special character, and be between 10 to 20 characters long."
			);
		}

		//TODO: check username does not already exist!!
		if (!validateEmailAddress(email)) {
			addErrorMessageToErrorList("Invalid email address.");
		}		

		if (!isInListOfAcceptedSignupCountries(country)) {
			addErrorMessageToErrorList("Invalid country input.");
		}
		
		if (!isValidCity(city)) {
			addErrorMessageToErrorList("Invalid city. Cities can only have alphabetic characters, hyphens and apostrophes. First character of each word must be uppercase");
		}
	}

	
	private boolean formContainsNullOrEmptyEntries() {	
		if (Utils.isNullOrEmpty(signupForm.getName())) {
			addErrorMessageToErrorList("Name must contain a value");
		}
		
		if (Utils.isNullOrEmpty(signupForm.getCity())) {
			addErrorMessageToErrorList("City must contain a value");
		}
		
		if (Utils.isNullOrEmpty(signupForm.getCountry())) {
			addErrorMessageToErrorList("Country must contain a value");
		}	
		
		if (Utils.isNullOrEmpty(signupForm.getEmail())) {
			addErrorMessageToErrorList("Email must contain a value");
		}
		
		if (Utils.isNullOrEmpty(signupForm.getPassword())) {
			addErrorMessageToErrorList("Password must contain a value");
		}
		
		if (Utils.isNullOrEmpty(signupForm.getPasswordRepeated())) {
			addErrorMessageToErrorList("Password repeated entry must contain a value");
		}
		
		return containsErrors();
	}

	// Password must contain at least one uppercase character, lower case character,
	// special character, and be between 10 to 20 characters long
	private boolean validatePassword(String password) {
		Pattern pattern = Pattern
				.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{10,20}$");
		Matcher matcher = pattern.matcher(password);
		boolean isMatchFound = matcher.find();

		return isMatchFound;
	}

	// Local-part: uppercase and lowercase Latin letters A to Z and a to z, digits:
	// 0-9, allow dot, underscore and hyphen, dot not first or last char and not
	// consecutive dots,
	// & max 64 characters
	// Domain: same as above for letters and digits and dots, hyphen is not first or
	// last character also
	// Regex is from:
	// https://mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	private boolean validateEmailAddress(String emailAddress) {
		Pattern pattern = Pattern.compile(
				"^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(emailAddress);
		boolean isMatchFound = matcher.find();
		
		return isMatchFound;
	}

	// https://stackoverflow.com/questions/712231/best-way-to-get-a-list-of-countries-in-java
	private boolean isInListOfAcceptedSignupCountries(String country) {
		String[] countryCodes = Locale.getISOCountries();

		for (String countryCode : countryCodes) {
			Locale locale = new Locale("", countryCode);

			if (country.equals(locale.getDisplayCountry())) {
				return true;
			}

		}
		
		return false;
	}

	// covers alpha only, double barrel (Bora-Bora), spaced city names and city names with apostrophes.
	// no longer than 20 characters, first character must be an uppercase letter, an apostrophe is optional after first letter of any word,
	// followed by one or more alpha character
	private boolean isValidCity(String city) {
		Pattern pattern = Pattern.compile(
				"(?=.{2,20}$)^([A-Z])(')?([a-z]+)((\\-|\\s)[A-Z](')?[a-z]+)*$");
		Matcher matcher = pattern.matcher(city);
		boolean isMatchFound = matcher.find();
		
		return isMatchFound;
	}

	public UserEntity convertSignupFormContentToUserEntity(SignUpFormRequest signupForm) {
		return new UserEntity();
	}

}
