package com.spfwproject.quotes.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spfwproject.quotes.models.LoginRequest;
import com.spfwproject.quotes.utils.Utils;

public class LoginValidator extends Validator {
	private Logger logger = LoggerFactory.getLogger(LoginValidator.class);

	private LoginRequest loginRequest;
	private final String INVALID_CREDENTIALS = "Credentials have been entered incorrectly.";

	public LoginValidator(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
	}

	@Override
	public void validate() {
		final String email = loginRequest.getUsername();
		final String password = loginRequest.getPassword();

		if (formContainsNullOrEmptyEntries()) {
			return;
		}

		if (!isValidPassword(password) || !isValidEmail(email)) {
			addErrorMessageToErrorList(INVALID_CREDENTIALS);
			return;
		}
	}

	private boolean formContainsNullOrEmptyEntries() {
		final String email = loginRequest.getUsername();
		final String password = loginRequest.getPassword();

		if (Utils.isNullOrEmpty(email) || Utils.isNullOrEmpty(password)) {
			addErrorMessageToErrorList(INVALID_CREDENTIALS);
			return true;
		}

		return containsErrors();
	}

	// Password must contain at least one uppercase character, lower case character,
	// special character, and be between 10 to 20 characters long
	private boolean isValidPassword(String password) {
		Pattern pattern = Pattern
				.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{10,20}$");
		Matcher matcher = pattern.matcher(password);
		boolean isValidPassword = matcher.find();

		return isValidPassword;
	}

	// Local-part: uppercase and lowercase Latin letters A to Z and a to z, digits:
	// 0-9, allow dot, underscore and hyphen, dot not first or last char and not
	// consecutive dots,
	// & max 64 characters
	// Domain: same as above for letters and digits and dots, hyphen is not first or
	// last character also
	// Regex is from:
	// https://mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	private boolean isValidEmail(String emailAddress) {
		Pattern pattern = Pattern.compile(
				"^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(emailAddress);
		boolean isValidEmail = matcher.find();

		return isValidEmail;
	}

}
