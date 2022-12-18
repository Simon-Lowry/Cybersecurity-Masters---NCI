package com.spfwproject.quotes.componenttests;

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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.mock.web.MockHttpSession;


import com.spfwproject.quotes.entities.RoleEntity;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.InvalidSessionException;
import com.spfwproject.quotes.exceptions.MalformedTokenException;
import com.spfwproject.quotes.exceptions.NonEntityOwnerAuthorisationException;
import com.spfwproject.quotes.models.LockUserRequest;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.utils.TestAuthInfo;
import com.spfwproject.quotes.utils.TestUtils;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrivilegeEscalationAndRoutingComponentTest {
	private Logger logger = LoggerFactory.getLogger(PrivilegeEscalationAndRoutingComponentTest.class);

	@Autowired
	UserDBAccess userDetailsService;
	 
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
	private TestUtils testUtils;
  
    @Autowired
	private ObjectMapper objectMapper;
        
	private static UserDetailsRequest signUpForm;
	
	private TestAuthInfo authInfo;

	
	@BeforeAll
	public void setup() throws JsonProcessingException, Exception {
		signUpForm = new UserDetailsRequest("My Name", "myemail@mail.com", "Passcword123&%", "Passcword123&%", "Dublin",
				"Ireland");	
		authInfo = testUtils.loginAndReturnAuthInfo();
	}

    @Test
    @WithAnonymousUser
    public void whenNotAuthenticated_thenAllowSignupButNoAdminOrUserEndpointsAllowed() throws Exception {
    	String testName = "whenNotAuthenticated_thenAllowSignupButNoAdminOrUserEndpointsAllowed";
    	logger.info("Initating test: " + testName);

		signUpForm.setUsername("");		
		
		ResultActions resultActions = mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpForm))).andExpect(status().isBadRequest());
    	MvcResult result = resultActions.andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("Completed: check non-authenticated user can reach signup form " + contentAsString);
         
         Exception exception = assertThrows(MalformedTokenException.class, () ->{ 
        	 mockMvc.perform(get("/admin/getUsers")
        			 .cookie(authInfo.getCookie()))
 				.andDo(MockMvcResultHandlers.print())
 				.andExpect(status().isUnauthorized()).andReturn();
 	    });
 	    String actualMessage = exception.getMessage();
 	    logger.info("Exception thrown message: " + actualMessage);
    	logger.info("Completed: check non-authenticated user can not reach admin page: " + contentAsString);

    	 assertThrows(MalformedTokenException.class, () ->{ 
        	 mockMvc.perform(get("/users/505")
        			 .cookie(authInfo.getCookie()))
 				.andDo(MockMvcResultHandlers.print())
 				.andExpect(status().isUnauthorized()).andReturn();
 	    });	 
     	logger.info("Completed: check non-authenticated user can not retrieve user data");


        UserEntity userEntity = new UserEntity();
        assertThrows(MalformedTokenException.class, () ->{ 
        	 mockMvc.perform(put("/users").cookie(authInfo.getCookie()).contentType(MediaType.APPLICATION_JSON)
         			.content(objectMapper.writeValueAsString(userEntity)))
         			.andExpect(status().isUnauthorized());
	    });	
    	logger.info("Completed: check non-authenticated user can not update any user");
        
    	assertThrows(MalformedTokenException.class, () ->{ 
    		mockMvc.perform(delete("/users/505").cookie(authInfo.getCookie())).andExpect(status().isUnauthorized());
 	    });	
    	logger.info("Completed: check non-authenticated user can not update any user");

    	logger.info("Finished test: " + testName);
    }
    
    @Test
    @WithMockUser(username = "felord",password = "felord.cn",roles = {"ADMIN"})
    public void givenAuthenticatedAdmin_ThenRequestProtectedUrlsWithAdmin_Succeed() throws Exception {
    	String testName = "requestProtectedUrlWithAdmin";
    	logger.info("Beginning test: " + testName);
    	
		String token = testUtils.generateAdminToken();

    	// admin allowed URLs, expect 200, OK response
    	mockMvc.perform(get("/admin/getUser/505")
				.cookie(authInfo.getCookie())
    			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        		.andExpect(status().isOk());
		logger.info("Completed: check admin user can reach admin/getUser");
		
		LockUserRequest lockUser = new LockUserRequest(505L, false);
		mockMvc.perform(put("/admin/lockUser").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lockUser))
				.cookie(authInfo.getCookie())
    			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        		.andExpect(status().isOk());
		logger.info("Completed: check admin user can reach admin/lock");
		
    	// admin not allowed URLs, expect 403, forbidden response
		mockMvc.perform(get("/auth/logout")
				.cookie(authInfo.getCookie())
    			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        		.andExpect(status().isForbidden());
		logger.info("Completed: check admin user can not reach auth/logout");
		
		mockMvc.perform(get("/quotes")
				.cookie(authInfo.getCookie())
    			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isForbidden());
		logger.info("Completed: check admin user can not reach quotes/**");
		
		mockMvc.perform(get("/users/getUser/505")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isForbidden());
		logger.info("Completed: check admin user can not reach users/**");
    }
    
    @Test
    public void givenRegularUserAndAuthenticated_ThenRequestProtectedAdminUrls_ThrowException() throws JsonProcessingException, Exception {
    	String testName = "requestProtectedUrlWithUser";
    	logger.info("Starting test: " + testName);
    	String userToken = "Bearer " + authInfo.getToken();

    	signUpForm.setUsername("myusername1234@gmail.com");
    	
		mockMvc.perform(get("/admin/unlockAccount/1")
				.cookie(authInfo.getCookie())
			.header(HttpHeaders.AUTHORIZATION, userToken))
			.andExpect(status().isForbidden());
		logger.info("Completed: check regular user can not perform admin/unlockAccount");
		
		mockMvc.perform(get("/admin/getUser/1")
				.cookie(authInfo.getCookie())
			.header(HttpHeaders.AUTHORIZATION,  userToken))
        	.andExpect(status().isForbidden());
		logger.info("Completed: check regular user can not perform admin/getUser");
		
		mockMvc.perform(get("/admin/lockUser")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, userToken))
        	.andExpect(status().isForbidden());
		logger.info("Completed: check regular user can not perform admin/lockUser");
			
    }
    
    @Test
    public void givenUserRole_WhenAcessingUserAndQuotesAPIs_AllowActions() throws Exception
    {
    	String testName = "givenUserRole_WhenAcessingUserAndQuotesAPIs_AllowActions";
    	logger.info("Starting test: " + testName);
    	String userToken = "Bearer " + authInfo.getToken();

    	mockMvc.perform(get("/users/506")
				.cookie(authInfo.getCookie())
    			.header(HttpHeaders.AUTHORIZATION, userToken))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andReturn();
    	logger.info("Completed: check authenticated user can reach get user functionality");
        
		UserDetailsRequest userDetailsReq = new UserDetailsRequest(9999L, null, null, "somePassword", "somePassword", null, null );

    	mockMvc.perform(delete("/users")
				.cookie(authInfo.getCookie())
    			.header(HttpHeaders.AUTHORIZATION, userToken)
	    		.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDetailsReq)))	
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isForbidden())
				.andExpect(status().reason("Unauthorized access, only the entity owner can act on the entity"));
    	logger.info("Completed: check authenticated user can reach delete user functionality");
    	
    	mockMvc.perform(get("/quotes/3/23")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + authInfo.getToken()))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andReturn();
    	logger.info("Completed: check authenticated user can reach get quote functionality");
    	
    	logger.info("Exiting test: " + testName);
    }
    
    @AfterAll()
	void afterAll() {
		testUtils.resetTestSessionCreationTime();
	}
    
}
