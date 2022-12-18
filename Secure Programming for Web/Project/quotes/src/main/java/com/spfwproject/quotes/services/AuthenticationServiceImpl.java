package com.spfwproject.quotes.services;

import com.spfwproject.quotes.entities.LoginAttemptsEntity;

import java.net.URI;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.InvalidSessionException;
import com.spfwproject.quotes.exceptions.LoginAttemptsLimitReachedException;
import com.spfwproject.quotes.interfaces.AuthenticationService;
import com.spfwproject.quotes.interfaces.JWTTokenService;
import com.spfwproject.quotes.models.TokenResponse;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.models.UserResponse;
import com.spfwproject.quotes.validators.UserDetailsValidator;
import com.spwproject.quotes.dbaccesslayer.LoginAttemptsDBAccess;

@Component
public class AuthenticationServiceImpl implements AuthenticationProvider, AuthenticationService {
	private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
	private final String UI_IP = "0:0:0:0:0:0:0:1";

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired 
    private LoginAttemptsDBAccess loginAttemptsDbAccess; 

	@Autowired
	private final JWTTokenService jwtTokenService;
	
	@Autowired
	private final SessionServiceImpl sessionServiceImpl;

	public AuthenticationServiceImpl(UserServiceImpl userService, PasswordEncoder bcryptEncoder,
			LoginAttemptsDBAccess loginAttemptsDbAccess,JWTTokenServiceImpl jwtTokenService, SessionServiceImpl sessionServiceImpl ) {
		this.userService = userService;
		this.bcryptEncoder = bcryptEncoder;
		this.loginAttemptsDbAccess = loginAttemptsDbAccess;
		this.jwtTokenService = jwtTokenService;
		this.sessionServiceImpl = sessionServiceImpl;
	}

	public String generatePasswordWithBCrypt(String plainPassword) {
		String encodedPassword = bcryptEncoder.encode(plainPassword);

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
			if (signUpFormValidator.containsErrors() && 
					!signUpFormValidator.getListOfErrors().contains("Invalid username.")) {
				signUpFormValidator.addErrorMessageToErrorList("Invalid username.");
			}

		}

		logger.info("Exiting " + methodName);
		return signUpFormValidator;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String methodName = "authenticate";
		logger.info("Entering " + methodName + " in AuthService.");

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		logger.info("Attempting to retrieve user entity via username...");
		UserEntity user = userService.getUserByUsername(username);
		boolean isUserAccountLocked = user.isAccountLocked();
		logger.info("User succesfully retrieved: " + user.toString());
		ArrayList<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
        authoritiesList.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
		
		LoginAttemptsEntity loginAttempts = processAttemptedLogins(user);

		if (!isUserAccountLocked && isExpectedPassword(password, user.getPassword())) { // successful login
			logger.info("Exiting " + methodName + " in AuthService.");
			loginAttempts.setAttempts(0);
			loginAttemptsDbAccess.saveAttemptsToDb(loginAttempts);
			//user.setPassword(null);
			return new UsernamePasswordAuthenticationToken(user, password, authoritiesList);
		}else if (isUserAccountLocked) { // account locked, throw exception		
			throw new LoginAttemptsLimitReachedException();
		} else { // failed login, increment login fails counter & throw exception
			loginAttempts.incrementAtttempts();
			isOverAttemptedLoginsLimit(loginAttempts, user);
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
	
	public LoginAttemptsEntity processAttemptedLogins(UserEntity userEntity) throws LoginAttemptsLimitReachedException {
		final String  methodName = "processAttemptedLogins";
		logger.info("In method: " + methodName);
		String username = userEntity.getUsername();

		Optional<LoginAttemptsEntity> 
	      userAttempts = loginAttemptsDbAccess.getAttemptsByUsername(username); 
		
		LoginAttemptsEntity attempts = null;
		if (userAttempts.isPresent()) {
			 attempts = userAttempts.get();
			 isOverAttemptedLoginsLimit(attempts, userEntity);
		} else {
			 attempts = new LoginAttemptsEntity(username);
		}
		
		logger.info("Exiting method: " + methodName);
		return  attempts;
	}
	
	private void isOverAttemptedLoginsLimit(LoginAttemptsEntity attemptsEntity, UserEntity userEntity) {
		final String methodName = "isOverAttemptedLoginsLimit";
		logger.info("Enter method: " + methodName);
		int currentNumAttempts = attemptsEntity.getAttempts();
		
		 logger.info("CURRENT ATTEMPTS: " + currentNumAttempts);
		if (attemptsEntity.getAttempts() > 3) {
			boolean isAccountLockedUpdated = true;
			userEntity.setAccountLocked(true); 
			userService.updateUser(userEntity, isAccountLockedUpdated);
			logger.info("Exception occured: " + 
			"Login attempts have exceeded limit. Account is now blocked.");
			throw new LoginAttemptsLimitReachedException();
		} 
		logger.info("Exiting method: " + methodName);
	}
	
	
	public TokenResponse setResponseBodyAndSecurityContextWithAuthInfo(Long id, 
			Authentication authentication, HttpServletRequest request) {
		final String methodName = "setResponseBodyAndSecurityContextWithAuthInfo";
		logger.info("Enter method: " + methodName);

		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		
		 // Create a new session and add the security context.
		HttpSession oldSession = request.getSession(false);
		logger.info("Session id before change: " + oldSession.getId());
		request.changeSessionId();		
		
		HttpSession session = request.getSession(false);
	    if (session == null) throw new InvalidSessionException();
		logger.info("Session id after change: " + request.getSession(false).getId());

	    sessionServiceImpl.saveUserSession(session, id);
	    	    		
		String token = jwtTokenService.generateToken(authentication);
		TokenResponse tokenResponse = new TokenResponse(token, "Bearer", id);
		
		logger.info("Exiting method: " + methodName);
		return tokenResponse;
	}
	
	public void generateAltSessionCookie( HttpServletRequest request,
			HttpServletResponse responseHttp) {
		String sessionId = request.getSession(false).getId();

		Cookie cookie = new Cookie("SESSION_ID", sessionId);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(1000);
		cookie.setPath("/");

		cookie.setDomain("quotes.app.v1.local");

		responseHttp.addCookie(cookie);

	}
	

}
