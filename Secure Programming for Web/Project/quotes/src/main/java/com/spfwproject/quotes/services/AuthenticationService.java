package com.spfwproject.quotes.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.SignUpFormRequest;
import com.spfwproject.quotes.validators.SignUpFormValidator;


@Component
public class AuthenticationService implements AuthenticationProvider {
	private Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	private static final Random RANDOM = new SecureRandom();
	private static final int ITERATIONS = 1; // TODO: decide on value, was 1000
    private static final int KEY_LENGTH = 256;


	public ArrayList<byte[]> generatePasswordHashWithSalt(char[] passwordAsCharArray) {
		byte[] salt = getNextSalt();

		byte[] passwordHash = generatePasswordWithPDKDF2(passwordAsCharArray, salt);	
		
		ArrayList<byte[]> passwordAndSalt = new ArrayList<byte[]>(2);
		passwordAndSalt.add(passwordHash);
		passwordAndSalt.add(salt);
		
		return passwordAndSalt;
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
	
	private byte[] generatePasswordWithPDKDF2(final char[] password, byte[] salt) {
		try {
			return SecretKeyFactory.getInstance("PBKDF2WithHmacSha1")
					.generateSecret(new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH))
					.getEncoded();
		}catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedPasswordHash) {
		    byte[] pwdHash = generatePasswordWithPDKDF2(password, salt);
		    Arrays.fill(password, Character.MIN_VALUE);
		   
		    if (pwdHash.length != expectedPasswordHash.length) {
		    	return false;
		    }
		    
		    for (int i = 0; i < pwdHash.length; i++) {
		      if (pwdHash[i] != expectedPasswordHash[i]) {
		    	  return false;
		      }
		    }
		    return true;
	}
		
	//TODO: fill in validation signup form
	public SignUpFormValidator validateSignupForm(SignUpFormRequest signupForm) {
		SignUpFormValidator signUpFormValidator = new SignUpFormValidator(signupForm);		
		signUpFormValidator.validate();
		
		return signUpFormValidator;
	}
	
	// Password must contain at least one uppercase character, lower case character, special character, and be between 10 to 20 characters long
	// characters hyphen, apostrophe and hash (', -, #) are not allowed to protect against attacks
	private boolean validatePassword(String password) {
		Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()[{}]:;,?/*~$^+=<>]).{10,20}$");
	    Matcher matcher = pattern.matcher(password);
	    boolean isMatchFound = matcher.find();
	    
	    return isMatchFound;
	}
	
	public UserEntity convertSignupFormToUserEntity(SignUpFormRequest signupForm) {
		UserEntity user = new UserEntity();
		char[] passwordAsCharArray = signupForm.getPassword().toCharArray();
    	ArrayList<byte[]> passwordAndHash = generatePasswordHashWithSalt(passwordAsCharArray);
    	
    	logger.info("done generating hashed password and salt");
    	user.setHashedPassword(passwordAndHash.get(0)); // obtains and sets the hashed password
    	user.setSalt(passwordAndHash.get(1)); // obtains and sets the salt

		 return user;
	}
	

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		// fill in from https://www.tutorialspoint.com/spring_security/spring_security_form_login_with_database.htm#
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		// fill in from https://www.tutorialspoint.com/spring_security/spring_security_form_login_with_database.htm#
		return false;
	}	
	
}
