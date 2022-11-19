package com.spfwproject.quotes.integrationtests.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.models.LockUserRequest;
import com.spfwproject.quotes.models.UserDetailsRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private UserDetailsRequest signUpForm;

	private Logger logger = LoggerFactory.getLogger(AdminControllerIntegrationTest.class);

	@Test
    @WithMockUser(username = "mockAdmin",password = "mockAdminPassword",roles = {"ADMIN"})
	void testGetUser_WithAdminAndValidUserId_ExpectSuccess() throws JsonProcessingException, Exception {
		logger.info("Entered test: testGetUser_WithAdminAndValidUserId_ExpectSuccess");
		mockMvc.perform(get("/admin/getUser/505")).andExpect(status().isOk());
	}
	
	@Test
    @WithMockUser(username = "mockAdmin",password = "mockAdminPassword",roles = {"ADMIN"})
	void testLockUser_WithAdminWithLockingAndUnlockingAUser_ExpectSuccess() throws JsonProcessingException, Exception {
		logger.info("Entered test: testGetUser_WithAdminWithLockingAndUnlockingUser_ExpectSuccess");
		LockUserRequest lockUserRequest = new LockUserRequest(505L, true);
		mockMvc.perform(put("/admin/lockUser").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lockUserRequest))).andExpect(status().isOk());
	
		lockUserRequest.setLockUser(false);
		mockMvc.perform(put("/admin/lockUser").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lockUserRequest))).andExpect(status().isOk());
	}

}
