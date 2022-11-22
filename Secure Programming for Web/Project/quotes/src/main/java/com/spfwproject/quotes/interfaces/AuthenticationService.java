package com.spfwproject.quotes.interfaces;

import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.validators.UserDetailsValidator;

public interface AuthenticationService {
	public String generatePasswordWithBCrypt(String plainPassword);

	public boolean isExpectedPassword(String enteredPassword, String encodedPassword);

	public UserDetailsValidator validateSignupForm(UserDetailsRequest signupForm);

}
