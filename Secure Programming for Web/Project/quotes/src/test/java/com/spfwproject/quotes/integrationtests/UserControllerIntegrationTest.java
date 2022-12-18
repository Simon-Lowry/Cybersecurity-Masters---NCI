package com.spfwproject.quotes.integrationtests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.config.SpringSecurityTestConfig;
import com.spfwproject.quotes.constants.Roles;
import com.spfwproject.quotes.entities.RoleEntity;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.LoginRequest;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.utils.TestAuthInfo;
import com.spfwproject.quotes.utils.TestUser;
import com.spfwproject.quotes.utils.TestUsers;
import com.spfwproject.quotes.utils.TestUtils;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TestUtils testUtils;

	@Autowired
	private ObjectMapper objectMapper;
		
	private String token;
	
	private TestUser testUser;
	private TestAuthInfo authInfo;

	
	private static Logger logger = LoggerFactory.getLogger(AuthenticationControllerIntegrationTest.class);
	
	@BeforeAll
	public  void setup() throws JsonProcessingException, Exception {
		logger.info("Enter Setup for  UserControllerIntegrationTest");
		testUser = TestUsers.getTestUser1();
		token = testUtils.generateUserToken(testUser.getUsername());
	    logger.info("Setup complete, produced token: " + token);
		authInfo = testUtils.loginAndReturnAuthInfo();

	}
	
	@Test
	public void getUser_WithAuthenticatedAndAuthorisedUser_ThenSucceed() throws Exception {
		final String methodName = "getUser_WithAuthenticatedAndAuthorisedUser_ThenSucceed";
		logger.info("Entered test: getUser_WithAuthenticatedAndAuthorisedUser_ThenSucceed");
		
		testUtils.setTestSecurityContextWithAuthorisation(testUser.getUsername(), testUser.getPassword());
		MvcResult result = mockMvc.perform(get("/users/506")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andReturn();
		logger.info("Completed test " + methodName);
	}
	
	@Test
	public void getUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException() throws Exception {
		final String methodName = "getUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException";
		logger.info("Entered test: getUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException");
	        
		testUtils.setTestSecurityContextWithAuthorisation(testUser.getUsername(), testUser.getPassword());
		mockMvc.perform(get("/users/9999")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))	
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isForbidden())
				.andExpect(status().reason("Unauthorized access, only the entity owner can act on the entity"));		
		
		logger.info("Completed test " + methodName);
	}
	
	@Test
	public void deleteUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException() throws Exception {
		final String methodName = "deleteUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException";
		logger.info("Entered test: " + methodName);
		
		UserDetailsRequest userDetailsReq = new UserDetailsRequest(9999L, null, null, "somePassword", "somePassword", null, null );
 
		testUtils.setTestSecurityContextWithAuthorisation(testUser.getUsername(), testUser.getPassword());
		mockMvc.perform(delete("/users")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDetailsReq)))	
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isForbidden())
				.andExpect(status().reason("Unauthorized access, only the entity owner can act on the entity"));		
		
		logger.info("Completed test " + methodName);
	}
	
	@Test
	public void updateUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException() throws Exception {
		final String methodName = "updateUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException";
		logger.info("Entered test: " + methodName);
		UserDetailsRequest userDetailsReq = new UserDetailsRequest(9999L, null, null, "somePassword", "somePassword", null, null );
	        
		testUtils.setTestSecurityContextWithAuthorisation(testUser.getUsername(), testUser.getPassword());
		mockMvc.perform(put("/users")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDetailsReq)))	
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
