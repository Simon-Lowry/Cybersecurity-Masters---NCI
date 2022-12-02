package com.spfwproject.quotes.integrationtests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.aspectj.lang.annotation.Before;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.interfaces.JWTTokenService;
import com.spfwproject.quotes.models.LockUserRequest;
import com.spfwproject.quotes.models.UserDetailsRequest;
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

	private Logger logger = LoggerFactory.getLogger(AdminControllerIntegrationTest.class);
	
	@BeforeAll
	public void setup() {
		token = testUtils.generateAdminToken();
	}

	@Test
	void testGetUser_WithAdminAndValidUserId_ExpectSuccess() throws JsonProcessingException, Exception {
		logger.info("Entered test: testGetUser_WithAdminAndValidUserId_ExpectSuccess");
		mockMvc.perform(get("/admin/getUser/505")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
			.andExpect(status().isOk());
	}
	
	@Test
	void testLockUser_WithAdminWithLockingAndUnlockingAUser_ExpectSuccess() throws JsonProcessingException, Exception {
		logger.info("Entered test: testGetUser_WithAdminWithLockingAndUnlockingUser_ExpectSuccess");
		LockUserRequest lockUserRequest = new LockUserRequest(505L, true);
		mockMvc.perform(put("/admin/lockUser").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lockUserRequest))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isOk());
	
		lockUserRequest.setLockUser(false);
		mockMvc.perform(put("/admin/lockUser").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lockUserRequest))
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isOk());
	}

}
