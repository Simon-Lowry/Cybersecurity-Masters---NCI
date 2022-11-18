package com.spfwproject.quotes.integrationtests.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.models.LoginRequest;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.validators.UserDetailsValidator;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private UserDetailsRequest signUpForm;

	private Logger logger = LoggerFactory.getLogger(AuthenticationControllerIntegrationTest.class);

	@BeforeEach
	void setup() {
		signUpForm = new UserDetailsRequest("My Name", "myemail@mail.com", "Passcword123&%", "Passcword123&%", "Dublin",
				"Ireland");

	}

	@Test
	void testSignup_WithValidSignupData_Success() throws JsonProcessingException, Exception {
		logger.info("Entered test: testSignup_WithValidSignupData_Success");
		signUpForm.setUsername("success" + signUpForm.getUsername());
		mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpForm))).andExpect(status().isCreated());
	}

	@Test
	void testSignup_WithAnyEmptySignupData_ThrowBadRequestException() throws JsonProcessingException, Exception {
		String methodName = "testSignup_WithAnyEmptySignupData_ThrowBadRequestException";
		logger.info("Running test: " + methodName);

		String password = "Passcword123&%";
		String name = "My Name";
		String email = "myemail@mail.com";
		int counter = 0;
		String city = "Dublin";
		String country = "Ireland";

		signUpForm.setPassword(null);
		signUpForm.setUsername(counter++ + "myemail@mail.com");
		expectBadRequestException(signUpForm);
		signUpForm.setUsername(counter++ + "myemail@mail.com");
		signUpForm.setPassword("");
		expectBadRequestException(signUpForm);

		// Password must contain a value

		signUpForm.setPassword(password);
		signUpForm.setName(null);
		signUpForm.setUsername(counter++ + "myemail@mail.com");
		expectBadRequestException(signUpForm);
		signUpForm.setName("");
		signUpForm.setUsername(counter++ + "myemail@mail.com");
		expectBadRequestException(signUpForm);

		signUpForm.setName(name);
		signUpForm.setPasswordRepeated(null);
		signUpForm.setUsername(counter++ + "myemail@mail.com");
		expectBadRequestException(signUpForm);
		signUpForm.setPasswordRepeated("");
		signUpForm.setUsername(counter++ + "myemail@mail.com");
		expectBadRequestException(signUpForm);

		signUpForm.setPasswordRepeated(password);
		signUpForm.setCity(null);
		signUpForm.setUsername(counter++ + "myemail@mail.com");
		expectBadRequestException(signUpForm);
		signUpForm.setCity("");
		signUpForm.setUsername(counter++ + "myemail@mail.com");
		expectBadRequestException(signUpForm);

		signUpForm.setCity(city);
		signUpForm.setCountry(null);
		signUpForm.setUsername(counter++ + "myemail@mail.com");
		expectBadRequestException(signUpForm);
		signUpForm.setCountry("");
		signUpForm.setUsername(counter++ + "myemail@mail.com");
		expectBadRequestException(signUpForm);

	}

	@Test
	void testSignup_WithBadPasswords_ThrowBadRequestExceptionWithCorrectErrorMessage()
			throws JsonProcessingException, Exception {
		signUpForm.setPassword("badPassword");
		signUpForm.setPasswordRepeated("badPassword");
		signUpForm.setUsername("ThrowBadRequest" + signUpForm.getUsername());
		String requestResponseBody = expectBadRequestException(signUpForm);
		requestResponseBody = requestResponseBody.replace("[", "").replace("]", "").replace("\"", "");

		assertEquals(requestResponseBody, UserDetailsValidator.PASSWORD_CONTENT_ERROR);
	}

	@Test
	void testSignup_WithNotMatchingPasswords_ThrowBadRequestExceptionWithCorrectErrorMessage()
			throws JsonProcessingException, Exception {
		signUpForm.setPassword("badPassword19090*");
		signUpForm.setPasswordRepeated("badPasswordll19000*");
		String requestResponseBody = expectBadRequestException(signUpForm);
		requestResponseBody = requestResponseBody.replace("[", "").replace("]", "").replace("\"", "");

		assertEquals(requestResponseBody, UserDetailsValidator.PASSWORD_REPEAT_ERROR);
	}

	/*
	 * @Test void testLogin_WithValidCredentials_Success() throws
	 * JsonProcessingException, Exception {
	 * 
	 * signUpForm.setUsername("words" + signUpForm.getUsername());
	 * mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
	 * .content(objectMapper.writeValueAsString(signUpForm))).andExpect(status().
	 * isCreated());
	 * 
	 * LoginRequest loginRequest = new LoginRequest(signUpForm.getUsername(),
	 * signUpForm.getPassword());
	 * mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
	 * .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().
	 * isOk());
	 * 
	 * }
	 */

	@Test
	void testLogin_WithBadCredentials_ThrowAuthenticationException() throws JsonProcessingException, Exception {
		signUpForm.setUsername("auth" + signUpForm.getUsername());
		mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpForm))).andExpect(status().isCreated());

		LoginRequest loginRequest = new LoginRequest(signUpForm.getUsername(), "JAmmm9$iijsjskskkkk");
		ResultActions resultActions = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)));
		MvcResult result = resultActions.andReturn();
		logger.info("status of bad creds test 1: " + result.getResponse());

		loginRequest = new LoginRequest("Bjork", signUpForm.getPassword());
		resultActions = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)));
		result = resultActions.andReturn();

		logger.info("status of bad creds test 2: " + result.getResponse());

		// TODO: check dups username is working, add in cleanup after each signup to
		// delete the entry
	}

	private String expectBadRequestException(UserDetailsRequest signUpForm) throws JsonProcessingException, Exception {
		ResultActions resultActions = mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpForm))).andExpect(status().isBadRequest());
		MvcResult result = resultActions.andReturn();
		String contentAsString = result.getResponse().getContentAsString();

		return contentAsString;
	}

}
