package com.spfwproject.quotes.integrationtests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.models.QuoteRequest;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.utils.TestAuthInfo;
import com.spfwproject.quotes.utils.TestUser;
import com.spfwproject.quotes.utils.TestUsers;
import com.spfwproject.quotes.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuotesControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Logger logger = LoggerFactory.getLogger(QuotesControllerIntegrationTest.class);
	
	@Autowired
	private TestUtils testUtils;
		
	private String token;
		
	private TestAuthInfo authInfo;
	
	private TestUser user;
	
	@BeforeAll
	public  void setup() throws JsonProcessingException, Exception {
		logger.info("Enter Setup for  UserControllerIntegrationTest");
		user = TestUsers.getTestUser3();
		authInfo = testUtils.loginAndReturnAuthInfo(user.getUsername(), user.getPassword());
	    logger.info("Setup complete, produced token: " + token);
	}
	
	@Test
	public void getQuote_WithAuthenticatedAndAuthorisedUser_ThenSucceed() throws Exception {
		final String methodName = "getQuote_WithAuthenticatedAndAuthorisedUser_ThenSucceed";
		logger.info("Entered test: " + methodName);
		
		MvcResult result = mockMvc.perform(get("/quotes/" + user.getUserId() + "/147")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + authInfo.getToken()))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andReturn();
		logger.info("Completed test " + methodName);
	}
	
	@Test
	public void getQuote_WithNotAuthorisedUser_ThenThrowException() throws Exception {
		final String methodName = "getQuote_WithAuthenticatedButNotAuthorisedQuote_ThenThrowException";
		logger.info("Entered test: " + methodName);
	        
		mockMvc.perform(get("/quotes/" +44L + "/147")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + authInfo.getToken()))	
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isForbidden())
				.andExpect(status().reason("Unauthorized access, only the entity owner can act on the entity"));		
		
		// QuoteRequest quoteRequest
		logger.info("Completed test " + methodName);
	}
	
	@Test
	@Transactional
	public void createQuote_WithLegitData_ThenSucceed() throws Exception {
		final String methodName = "getQuote_WithAuthenticatedButNotAuthorisedQuote_ThenThrowException";
		logger.info("Entered test: " + methodName);
	        
		QuoteRequest quoteRequest = new QuoteRequest(null, user.getUserId(), "Some quote", "PRIVATE", "John Bird");

		mockMvc.perform(post("/quotes")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + authInfo.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(quoteRequest)))	
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isCreated());
		
		logger.info("Completed test " + methodName);
	}
	
	@Test
	public void updateQuote_WithNotAuthorisedUser_ThenThrowException() throws Exception {
		final String methodName = "getQuote_WithAuthenticatedButNotAuthorisedQuote_ThenThrowException";
		logger.info("Entered test: " + methodName);
	        
		QuoteRequest quoteRequest = new QuoteRequest(147L, 5506L, "Some quote", "PRIVATE", "John Bird");
		
		logger.info("Attempting to delete quote....");
		mockMvc.perform(put("/quotes")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + authInfo.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(quoteRequest)))	
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isForbidden())
				.andExpect(status().reason("Unauthorized access, only the entity owner can act on the entity"));		
			
		logger.info("Completed test " + methodName);
	}
	
	@Test
	@Transactional
	public void deleteQuote_WithLegitData_ThenSucceed() throws Exception {
		final String methodName = "deleteQuote_WithLegitData_ThenSucceed";
		logger.info("Entered test: " + methodName);
	      
		QuoteRequest quoteRequest = new QuoteRequest(147L, user.getUserId(), "Some quote", "PRIVATE", "John Bird");
		
		mockMvc.perform(delete("/quotes")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + authInfo.getToken())
				.cookie(authInfo.getCookie())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(quoteRequest)))	
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk());
		
		logger.info("Completed test " + methodName);
	}
	
	@Test
	public void deleteQuote_WithNotAuthorisedUser_ThenThrowException() throws Exception {
		final String methodName = "deleteQuote_WithNotAuthorisedUser_ThenThrowException";
		logger.info("Entered test: " + methodName);
	        
		QuoteRequest quoteRequest = new QuoteRequest(147L, 191L, "Some quote", "PRIVATE", "John Bird");
		logger.info("Attempting to delete quote....");

		mockMvc.perform(delete("/quotes")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + authInfo.getToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(quoteRequest)))	
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isForbidden())
				.andExpect(status().reason("Unauthorized access, only the entity owner can act on the entity"));		
			
		logger.info("Completed test " + methodName);
	}
	
	@AfterAll()
	void afterAll() {
		testUtils.resetTestSessionCreationTime();
	}

}
