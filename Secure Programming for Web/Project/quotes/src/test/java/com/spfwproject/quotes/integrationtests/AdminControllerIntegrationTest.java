package com.spfwproject.quotes.integrationtests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Before;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.interfaces.JWTTokenService;
import com.spfwproject.quotes.models.LockUserRequest;
import com.spfwproject.quotes.models.LoginRequest;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.utils.TestAuthInfo;
import com.spfwproject.quotes.utils.TestUser;
import com.spfwproject.quotes.utils.TestUsers;
import com.spfwproject.quotes.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdminControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
		
	@Autowired
	private TestUtils testUtils;
	
	String token;
	
	private TestAuthInfo authInfo;


	private Logger logger = LoggerFactory.getLogger(AdminControllerIntegrationTest.class);
	
	@BeforeAll
	public void setup() throws JsonProcessingException, Exception {
		token = testUtils.generateAdminToken();		
		authInfo = testUtils.loginAndReturnAuthInfo();
	}

	@Test
    @WithMockUser(username = "felord",password = "felord.cn",roles = {"ADMIN"})
	void testGetUser_WithAdminAndValidUserId_ExpectSuccess() throws JsonProcessingException, Exception {
		String testName = "testGetUser_WithAdminAndValidUserId_ExpectSuccess";
		logger.info("Entered test: " + testName);
		mockMvc.perform(get("/admin/getUser/505")
				.cookie(authInfo.getCookie())
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
			.andExpect(status().isOk());
		logger.info("Completed test " + testName);
	}
	
	@Test
    @WithMockUser(username = "felord",password = "felord.cn",roles = {"ADMIN"})
	void testLockUser_WithAdminWithLockingAndUnlockingAUser_ExpectSuccess() throws JsonProcessingException, Exception {
		String testName = "testLockUser_WithAdminWithLockingAndUnlockingAUser_ExpectSuccess";
		logger.info("Entered test: " + testName);
		LockUserRequest lockUserRequest = new LockUserRequest(505L, true);
		mockMvc.perform(put("/admin/lockUser").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lockUserRequest))
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isOk());
	
		lockUserRequest.setLockUser(false);
		mockMvc.perform(put("/admin/lockUser").contentType(MediaType.APPLICATION_JSON)
				.cookie(authInfo.getCookie())
				.content(objectMapper.writeValueAsString(lockUserRequest))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isOk());
		logger.info("Completed test " + testName);

	}
	
	 @AfterAll()
		void afterAll() {
			testUtils.resetTestSessionCreationTime();
		}

}
