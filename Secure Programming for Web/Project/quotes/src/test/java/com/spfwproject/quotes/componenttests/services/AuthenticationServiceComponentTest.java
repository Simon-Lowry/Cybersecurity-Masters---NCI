package com.spfwproject.quotes.componenttests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spfwproject.quotes.models.SignUpFormRequest;
import com.spfwproject.quotes.services.AuthenticationService;
import com.spfwproject.quotes.validators.SignUpFormValidator;

@SpringBootTest
public class AuthenticationServiceComponentTest {
	@Autowired
	AuthenticationService authenticationService;
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationServiceComponentTest.class);

	@Test
	void testValidateSignupForm() {
		
		SignUpFormRequest signUpForm = new SignUpFormRequest("Some Name", "words@email.com", 
				"Password123*", "Password123*", "Dublin", "Ireland");
		
		// valid signup form, expect no errors
		SignUpFormValidator validator = authenticationService.validateSignupForm(signUpForm);
		logger.info("Expect valid:");
		logger.info(validator.toString());
		assertFalse(validator.containsErrors());
		
		// test form validation on passwords
		assertChecksOnPasswordsForSignupFormValidation(validator, signUpForm);
		
	}
	
	
	private void assertChecksOnPasswordsForSignupFormValidation(SignUpFormValidator validator, SignUpFormRequest signUpForm) {
		final String passwordErrorExpected = SignUpFormValidator.PASSWORD_CONTENT_ERROR;
		final String passwordRepeatError = SignUpFormValidator.PASSWORD_REPEAT_ERROR;
		
		// use different password, expect error
		signUpForm.setPasswordRepeated("abc");
		validator = authenticationService.validateSignupForm(signUpForm);
		assertTrue(validator.containsErrors());
		assertTrue(validator.getListOfErrors().contains(passwordRepeatError));

		// use a short password, expect error
		setPasswordAndRepeatPassword("short", signUpForm);
		validator = authenticationService.validateSignupForm(signUpForm);
		assertTrue(validator.containsErrors());
		assertTrue(validator.getListOfErrors().contains(passwordErrorExpected));
		
		// use lowercase only password, expect error
		setPasswordAndRepeatPassword("abcdefghillojoijojjijio", signUpForm);
		validator = authenticationService.validateSignupForm(signUpForm);
		assertTrue(validator.containsErrors());
		assertTrue(validator.getListOfErrors().contains(passwordErrorExpected));
		
		// uppercase, number, length but no special character, expect error
		setPasswordAndRepeatPassword("Abcdefghillojoijojjijio1", signUpForm);
		validator = authenticationService.validateSignupForm(signUpForm);
		assertTrue(validator.containsErrors());
		assertTrue(validator.getListOfErrors().contains(passwordErrorExpected));
		
		// use different password, expect error
		signUpForm.setPasswordRepeated("abc");
		validator = authenticationService.validateSignupForm(signUpForm);
		assertTrue(validator.containsErrors());
		assertTrue(validator.getListOfErrors().contains(passwordErrorExpected));
		
		// use a null password, expect error
		signUpForm.setPasswordRepeated(null);
		validator = authenticationService.validateSignupForm(signUpForm);
		assertTrue(validator.containsErrors());
	    assertTrue(validator.getListOfErrors().contains("Password repeated entry must contain a value"));
	}
		
	private SignUpFormRequest setPasswordAndRepeatPassword(String password, SignUpFormRequest signUpForm) {
		signUpForm.setPassword(password);
		signUpForm.setPasswordRepeated(password);
		return signUpForm;
	}
		
	@Test
	void testGeneratePasswordHashWithSalt() {
		String password = "SomePassword";
		char[] passwordAsCharArray = password.toCharArray();
		
		ArrayList<byte[]> result = authenticationService.generatePasswordHashWithSalt(passwordAsCharArray);
	
		assertNotNull(result);
		assertEquals(result.get(0).length, 32);
		
		byte[] hashedPassword = result.get(0);
		byte[] salt = result.get(1);
		
		// ensure password generated with hash matches when generated twice with the same salt
		boolean isEqualPassword = authenticationService.isExpectedPassword(passwordAsCharArray, salt, hashedPassword);
		assertTrue(isEqualPassword);
	}
	
	
	void testIsExpectedPassword() {
		String password = "SomePassword";
		char[] passwordAsCharArray = password.toCharArray();
		
		ArrayList<byte[]> result = authenticationService.generatePasswordHashWithSalt(passwordAsCharArray);

		byte[] hashedPassword = result.get(0);
		byte[] salt = null;


		// legitimate password but null salt
		boolean isEqualPassword = authenticationService.isExpectedPassword(passwordAsCharArray, salt, hashedPassword);
		assertFalse(isEqualPassword);
		
		
	}
}
