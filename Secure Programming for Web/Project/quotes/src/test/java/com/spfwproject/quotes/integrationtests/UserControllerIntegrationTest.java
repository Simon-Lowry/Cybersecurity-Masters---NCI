package com.spfwproject.quotes.integrationtests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.config.SpringSecurityTestConfig;
import com.spfwproject.quotes.constants.Roles;
import com.spfwproject.quotes.entities.RoleEntity;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	UserDBAccess userDetailsService;
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationControllerIntegrationTest.class);
	
	@Test
	public void getUser_WithAuthenticatedAndAuthorisedUser_ThenSucceed() throws Exception {
		logger.info("Entered test: testGetUser_WithAdminAndValidUserId_ExpectSuccess");
		UserEntity user = (UserEntity) userDetailsService.loadUserByUsername("john@gmail.com");
	        	   
		mockMvc.perform(get("/users/505")
				.with(user(user)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException() throws Exception {
		logger.info("Entered test: getUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException");
		UserEntity user = (UserEntity) userDetailsService.loadUserByUsername("bart@gmail.com");
	        
		mockMvc.perform(get("/users/505")
				.with(user(user)))
				.andExpect(status().isForbidden());
	}
	
	//TODO: need to fix update first and change userdetailsrequest for request here
	/*
	@Test
	public void updateUser_WithAuthenticatedAndAuthorisedUser_ThenSucceed() throws Exception {
		logger.info("Entered test: updateUser_WithAuthenticatedAndAuthorisedUser_ThenSucceed");
		UserEntity user = (UserEntity) userDetailsService.loadUserByUsername("john@gmail.com");
		UserDetailsRequest someUserDetailsRequest = new UserDetailsRequest();
		mockMvc.perform(put("/users")
				.with(user(user)).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(someUserDetailsRequest)))
				.andExpect(status().isOk());
	}
	*/
	
	@Test
	public void updateUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException() throws Exception {
		logger.info("Entered test: updateUser_WithAuthenticatedButNotAuthorisedUser_ThenThrowException");
	    UserEntity user = (UserEntity) userDetailsService.loadUserByUsername("bart@gmail.com");
		UserDetailsRequest someUserDetailsRequest = new UserDetailsRequest();
		someUserDetailsRequest.setId(505L);
		
		mockMvc.perform(put("/users")
				.with(user(user))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(someUserDetailsRequest)))
				.andExpect(status().isForbidden());
	}

}
