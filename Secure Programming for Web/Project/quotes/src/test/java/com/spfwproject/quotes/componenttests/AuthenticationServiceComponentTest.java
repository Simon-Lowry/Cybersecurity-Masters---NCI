package com.spfwproject.quotes.componenttests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.services.AuthenticationServiceImpl;
import com.spfwproject.quotes.utils.TestUser;
import com.spfwproject.quotes.utils.TestUsers;
import com.spfwproject.quotes.utils.TestUtils;
import com.spfwproject.quotes.validators.UserDetailsValidator;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationServiceComponentTest {
	@Autowired
	AuthenticationServiceImpl authenticationService;
	
	@Autowired
	private TestUtils testUtils;
	
	private TestUser testUser;
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationServiceComponentTest.class);
	
	@BeforeAll
	public void setup() {
		testUser = TestUsers.getTestUser1();
	}

	@Test
	void testValidateSignupForm() {
		String password = "(mWoringstuf)1n4a";
		
		UserDetailsRequest signUpForm = new UserDetailsRequest("Some Name", "words@email.com", 
				password, password, "Dublin", "Ireland");
		
		// valid signup form, expect no errors
		UserDetailsValidator validator = authenticationService.validateSignupForm(signUpForm);
		logger.info("Expect valid:");
		logger.info(validator.toString());
		assertFalse(validator.containsErrors());
		
		// test form validation on passwords
		assertChecksOnPasswordsForSignupFormValidation(validator, signUpForm);
		
	}
	
	private void assertChecksOnPasswordsForSignupFormValidation(UserDetailsValidator validator, UserDetailsRequest signUpForm) {
		final String passwordErrorExpected = UserDetailsValidator.PASSWORD_CONTENT_ERROR;
		final String passwordRepeatError = UserDetailsValidator.PASSWORD_REPEAT_ERROR;
		
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
		
		// 𝘈Ḇ𝖢𝕯٤ḞԍНǏ𝙅ƘԸⲘ𝙉০Ρ𝗤Ɍ𝓢ȚЦ𝒱Ѡ𝓧ƳȤ
		// use non-ASCII characters, expect error
		setPasswordAndRepeatPassword("𝘈Ḇ𝖢𝕯٤ḞԍНǏ𝙅ƘԸⲘ𝙉০Ρ𝗤Ɍ𝓢ȚЦ𝒱Ѡ𝓧ƳȤ", signUpForm);
		validator = authenticationService.validateSignupForm(signUpForm);
		assertTrue(validator.containsErrors());
		assertTrue(validator.getListOfErrors().contains(passwordErrorExpected));
		
		// use more non-ASCII characters, expect error
		setPasswordAndRepeatPassword("ᒪ੦ꞧ𝒆ṁ ірʂǚм ḋởƚꝍȑ 𐑈ĭţ 𝗮ḿě𝘁, çó𝖓ṣℯ𝔠тệ𝘵𝚞ṝ åᵭȉƥîŝćí𝞹𝗀 ɇł𝒾ţ, "
				+ "𝓈ę𝘥 𝚍ṏ 𝙚𝒊ůṥɱởⅾ 𝘁ếᶆ𝕡𝐨ṛ 𝞲𝑛ϲ𝒊ᶁ𝙞đȗռ𝞽 μť ⱡ⍺бỡ𝗋ȅ ɇ𝛕 𝖉۵ȴờ𝗿ε м𝞪𝓰𝓃ǟ ⱥɫ𝝸ʠưá.", signUpForm);
		validator = authenticationService.validateSignupForm(signUpForm);
		assertTrue(validator.containsErrors());
		assertTrue(validator.getListOfErrors().contains(passwordErrorExpected));
		
		// use different password, expect error
		signUpForm.setPasswordRepeated("abc");
		validator = authenticationService.validateSignupForm(signUpForm);
		assertTrue(validator.containsErrors());
		assertTrue(validator.getListOfErrors().contains(passwordErrorExpected));
		
		// use a null password, expect error
		signUpForm.setPassword(null);
		validator = authenticationService.validateSignupForm(signUpForm);
		assertTrue(validator.containsErrors());
		logger.info("Errors test 1: " + validator.getListOfErrors());
	    assertTrue(validator.getListOfErrors().contains(UserDetailsValidator.PASSWORD_NOT_SET_ERROR));
	    
	    // use an empty password, expect error
	 	signUpForm.setPassword("");
	 	validator = authenticationService.validateSignupForm(signUpForm);
	 	assertTrue(validator.containsErrors());
	 	assertTrue(validator.getListOfErrors().contains(UserDetailsValidator.PASSWORD_NOT_SET_ERROR));
	 	
	 	// use password that contains password common password list, expect error
		setPasswordAndRepeatPassword("letmein12345A*", signUpForm);
	 	validator = authenticationService.validateSignupForm(signUpForm);
	 	assertTrue(validator.containsErrors());
	 	assertTrue(validator.getListOfErrors().contains(UserDetailsValidator.COMMONLY_USED_PASSWORD_ERROR));
	 	
	}
		
	private UserDetailsRequest setPasswordAndRepeatPassword(String password, UserDetailsRequest signUpForm) {
		signUpForm.setPassword(password);
		signUpForm.setPasswordRepeated(password);
		return signUpForm;
	}
	
	// -------------------------------------------------
	// Sample bcrypt password:
	// $2a$10$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW
	//--------------------------------------------------
	// first $ to second dollar sign contains the algorithm version used by the bcrpt encoder. Here that's 2a.
	// Second dollar sign to third is cost/work factor used. Here that's 10 for the work factor.
	// Beyond that is the hash and then the password. The salt will be random and the hash not predictable.
	// Both the algorithm and the work factor's inclusion in the string can be checked.
	@Test
	void testGeneratePasswordHashAndItsContentsAndIsPlaintextPasswordHashedEqualsToIt() {
		String password = "SomePassword";
		
		testUtils.setTestSecurityContextWithAuthorisation(testUser.getUsername(), testUser.getPassword());
		
		String result = authenticationService.generatePasswordWithBCrypt(password);
		logger.info("Generated password hash: " + result);
		assertNotNull(result);
		
		// check for algorithm
		String algorithm = result.substring(0, 3);
		logger.info("Algorithm: " + algorithm);
		assertEquals(algorithm, "$2a");
		
		// check for work factor
		String workFactor = result.substring(3, 6);
		logger.info("Work Factor: " + workFactor);
		assertEquals(workFactor, "$10");
		
		// ensure password generated with bcrypt hash matches plaintext password hashed with bcrypt as well 
		boolean isEqualPassword = authenticationService.isExpectedPassword(password, result);
		assertTrue(isEqualPassword);
	}
	

}
