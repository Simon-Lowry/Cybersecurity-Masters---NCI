package com.spfwproject.quotes.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationService implements AuthenticationProvider {
	private Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	private static final Random RANDOM = new SecureRandom();
	private static final int ITERATIONS = 1;
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
	
	public boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
		    byte[] pwdHash = generatePasswordWithPDKDF2(password, salt);
		    Arrays.fill(password, Character.MIN_VALUE);
		    if (pwdHash.length != expectedHash.length) return false;
		    for (int i = 0; i < pwdHash.length; i++) {
		      if (pwdHash[i] != expectedHash[i]) return false;
		    }
		    return true;
	}
		
	//TODO: fill in validation signup form
	public boolean validateSignupForm() {
		return true;
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
