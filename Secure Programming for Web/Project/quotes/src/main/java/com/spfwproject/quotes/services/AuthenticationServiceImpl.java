package com.spfwproject.quotes.services;

import com.spfwproject.quotes.entities.LoginAttemptsEntity;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.LoginAttemptsLimitReachedException;
import com.spfwproject.quotes.interfaces.AuthenticationService;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.validators.UserDetailsValidator;
import com.spwproject.quotes.dbaccesslayer.LoginAttemptsDBAccess;

@Component
public class AuthenticationServiceImpl implements AuthenticationProvider, AuthenticationService {
	private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	private static final Random RANDOM = new SecureRandom();
	private static final int ATTEMPTS_LIMIT = 3; 

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired 
    private LoginAttemptsDBAccess loginAttemptsDbAccess; 


	public AuthenticationServiceImpl(UserServiceImpl userService, PasswordEncoder bcryptEncoder,
			LoginAttemptsDBAccess loginAttemptsDbAccess) {
		this.userService = userService;
		this.bcryptEncoder = bcryptEncoder;
		this.loginAttemptsDbAccess = loginAttemptsDbAccess;
	}

	/**
	 * Returns a.....
	 *
	 * @return
	 */
	protected static byte[] getNextSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}

	public String generatePasswordWithBCrypt(String plainPassword) {
		String encodedPassword = bcryptEncoder.encode(plainPassword);

		logger.info("Password before bcrypt plain: " + plainPassword);
		logger.info("Encoded bcrypt password: " + encodedPassword);

		return encodedPassword;
	}

	

	public boolean isExpectedPassword(String enteredPassword, String encodedPassword) {
		// String enteredPasswordHashed = bcryptEncoder.encode(enteredPassword);
		// TODO: add logs
		return bcryptEncoder.matches(enteredPassword, encodedPassword);
	}

	public UserDetailsValidator validateSignupForm(UserDetailsRequest signupForm) {
		final String methodName = "validateSignupForm";
		logger.info("Entering " + methodName);

		UserDetailsValidator signUpFormValidator = new UserDetailsValidator(signupForm);
		signUpFormValidator.validate();

		if (userService.doesUsernameAlreadyExist(signupForm.getUsername())) {
			if (!signUpFormValidator.getListOfErrors().contains("Invalid username.")) {
				signUpFormValidator.addErrorMessageToErrorList("Invalid username.");
			}

		}

		logger.info("Exiting " + methodName);
		return signUpFormValidator;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String methodName = "authenticate";
		logger.info("Entering " + methodName);

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		logger.info("in authenticate pre get userbyusername");
		UserEntity user = userService.getUserByUsername(username);
		
		LoginAttemptsEntity loginAttempts = processAttemptedLogins(username);

		if (isExpectedPassword(password, user.getPassword())) { // successful login
			logger.info("Exiting " + methodName);

			// TODO: need to add roles to authorities instead of empty list
			return new UsernamePasswordAuthenticationToken(user, password, Collections.emptyList());
		} else { // failed login
			loginAttempts.incrementAtttempts();
			isOverAttemptedLoginsLimit(loginAttempts);
			loginAttemptsDbAccess.saveAttemptsToDb(loginAttempts);
			logger.info("Exiting " + methodName + ", throwing exception");
			throw new BadCredentialsException("Authentication failed." + 
					(4 - loginAttempts.getAttempts()) + " remaining attempts.");
		}
	}

	@Override
	public boolean supports(Class<?> authenticationType) {
		return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	public LoginAttemptsEntity processAttemptedLogins(String username) throws LoginAttemptsLimitReachedException {
		Optional<LoginAttemptsEntity> 
	      userAttempts = loginAttemptsDbAccess.getAttemptsByUsername(username); 
		
		LoginAttemptsEntity attempts = null;
		if (userAttempts.isPresent()) {
			 attempts = userAttempts.get();
			 isOverAttemptedLoginsLimit(attempts);
		} else {
			 attempts = new LoginAttemptsEntity(username);
		}
		return  attempts;
	}
	
	private void isOverAttemptedLoginsLimit(LoginAttemptsEntity attemptsEntity) {
		int currentNumAttempts = attemptsEntity.getAttempts();
		
		 logger.info("CURRENT ATTEMPTS: " + currentNumAttempts);
		if (attemptsEntity.getAttempts() > 3) {
			//TODO: set user to disabled, log failure and throw exception
			logger.info("Exception occured: " + 
			"Login attempts have exceeded limit. Account is now blocked.");
			throw new LoginAttemptsLimitReachedException();
		} 
	}

}
