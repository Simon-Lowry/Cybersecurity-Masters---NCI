package com.spfwproject.quotes.componenttests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.GetMapping;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.LockUserRequest;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


/**
 * Testing out authorization when it comes to the various roles and the
 * actions/api's they can access. This matches the security config.
 * 
 *  .mvcMatchers("/admin/**").hasRole("ADMIN")
 *  .mvcMatchers("/auth/logout").hasRole("USER")
 *  .mvcMatchers("/quotes/**").hasRole("USER")
 *  .mvcMatchers("/users/**").hasAnyRole("USER")
 *  .mvcMatchers("/auth/signUp").hasAnyRole("ANONYMOUS")
 *   .anyRequest().deny(); 
 *   
 *   Admins can access /admin controller only.
 *   Users can access 
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationEntryPointsComponentTest {
	private Logger logger = LoggerFactory.getLogger(ApplicationEntryPointsComponentTest.class);

	@Autowired
	UserDBAccess userDetailsService;
 
    @Autowired
    private MockMvc mockMvc;
  
    @Autowired
	private ObjectMapper objectMapper;
    
	private static UserDetailsRequest signUpForm;
	
	@BeforeAll
	public static void setup() {
		signUpForm = new UserDetailsRequest("My Name", "myemail@mail.com", "Passcword123&%", "Passcword123&%", "Dublin",
				"Ireland");		
	}

    @Test
    public void whenNotAuthenticated_thenAllowSignupButNoAdminOrUserEndpointsAllowed() throws JsonProcessingException, Exception {
    	String testName = "whenNotAuthenticated_thenAllowSignupButNoAdminOrUserEndpointsAllowed";
    	logger.info("Initating test: " + testName);

		signUpForm.setUsername("");		
		
		ResultActions resultActions = mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpForm))).andExpect(status().isBadRequest());
    	MvcResult result = resultActions.andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Completed: check non-authenticated user can reach signup form " + contentAsString);


        mockMvc.perform(get("/admin/getUsers")).andExpect(status().isUnauthorized());
    	logger.info("Completed: check non-authenticated user can not reach admin page");

        mockMvc.perform(get("/users/505")).andExpect(status().isUnauthorized());
    	logger.info("Completed: check non-authenticated user can not retrieve user data");

        UserEntity userEntity = new UserEntity();
        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
        			.content(objectMapper.writeValueAsString(userEntity)))
        			.andExpect(status().isUnauthorized());
    	logger.info("Completed: check non-authenticated user can not update any user");
        
        mockMvc.perform(delete("/users/505")).andExpect(status().isUnauthorized());
    	logger.info("Completed: check non-authenticated user can not update any user");

    	logger.info("Finished test: " + testName);
    }
    
    @Test
    @WithMockUser(username = "felord",password = "felord.cn",roles = {"ADMIN"})
    public void requestProtectedUrlWithAdmin() throws Exception {
    	String testName = "requestProtectedUrlWithAdmin";
    	logger.info("Beginning test: " + testName);
    	
    	// admin allowed URLs, expect 200, OK response
    	mockMvc.perform(get("/admin/getUser/505"))
        .andExpect(status().isOk());
		logger.info("Completed: check admin user can reach admin/getUser");
		
		LockUserRequest lockUser = new LockUserRequest(505L, false);
		mockMvc.perform(put("/admin/lockUser").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lockUser)))
        		.andExpect(status().isOk());
		logger.info("Completed: check admin user can reach admin/lock");
		
    	// admin not allowed URLs, expect 403, forbidden response
		mockMvc.perform(get("/auth/logout"))
        .andExpect(status().isForbidden());
		logger.info("Completed: check admin user can not reach auth/logout");
		
		mockMvc.perform(get("/quotes"))
        .andExpect(status().isForbidden());
		logger.info("Completed: check admin user can not reach quotes/**");
		
		mockMvc.perform(get("/users/getUser/505"))
        .andExpect(status().isForbidden());
		logger.info("Completed: check admin user can not reach users/**");
    }
    
    @Test
    public void requestProtectedAdminUrlsWithRegularUser_ThrowException() throws JsonProcessingException, Exception {
    	String testName = "requestProtectedUrlWithUser";
    	logger.info("Starting test: " + testName);
		UserEntity user = (UserEntity) userDetailsService.loadUserByUsername("john@gmail.com");

    	signUpForm.setUsername("myusername1234@gmail.com");
    	
		mockMvc.perform(get("/admin/unlockAccount/1")
			.with(user(user)))
			.andExpect(status().isForbidden());
		logger.info("Completed: check regular user can not perform admin/unlockAccount");
		
		mockMvc.perform(get("/admin/getUser/1")
			.with(user(user)))
        	.andExpect(status().isForbidden());
		logger.info("Completed: check regular user can not perform admin/getUser");
		
		mockMvc.perform(get("/admin/lockUser")
			.with(user(user)))
        	.andExpect(status().isForbidden());
		logger.info("Completed: check regular user can not perform admin/lockUser");
			
    	//TODO: check user can't access any quotes of anoter user
    	//TODO: check user can't access any admin functionality or signup form
  
    }
}
