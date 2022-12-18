package com.spfwproject.quotes.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.CoreMatchers.*;



import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.json.JSONException;
import org.json.JSONObject;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.support.WebTestUtils;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.entities.LoginAttemptsEntity;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.models.LoginRequest;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.services.AuthenticationServiceImpl;
import com.spfwproject.quotes.services.UserServiceImpl;
import com.spfwproject.quotes.utils.TestAuthInfo;
import com.spfwproject.quotes.utils.TestUser;
import com.spfwproject.quotes.utils.TestUsers;
import com.spfwproject.quotes.utils.TestUtils;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TestUtils testUtils;

	private UserDetailsRequest signUpForm;
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationControllerIntegrationTest.class);

	private String password = "Passcword12^&$";
	
	private TestAuthInfo authInfo;

	@BeforeAll
	void setupBeforeAll() throws JsonProcessingException, Exception {
		authInfo = testUtils.loginAndReturnAuthInfo();
	}
	
	@BeforeEach
	void setup() {
		
		signUpForm = new UserDetailsRequest("My Name", "myemail@mail.com", password,password, "Dublin",
				"Ireland");
	}

	@Test
	@WithAnonymousUser
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
	@WithAnonymousUser
	void testSignupAndLogout_WithLegitCredentials_ThenSucceed() throws JsonProcessingException, Exception {
		logger.info("Enter legit login: ");
		
		MvcResult result = mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpForm))).andExpect(status().isCreated()).andReturn();
		
		String token = testUtils.getTokenFromJSONResponse(result);
	
		logger.info("Attempt to logout....");
		result = mockMvc.perform(post("/auth/logout").cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))		
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andReturn();
		logger.info("Logout request success, test complete.");
	}
	
	@Test
	@WithAnonymousUser
	void testLogin_WithBadCredentials_ThrowAuthenticationException() throws JsonProcessingException, Exception {
		LoginRequest loginRequest = new LoginRequest("simon@gmail.com", "JAmmm9$iijsjskskkkk");
		ResultActions resultActions = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnauthorized());
		MvcResult result = resultActions.andReturn();
		logger.info("status of bad creds test 1: " + result.getResponse());

		loginRequest = new LoginRequest("Bjork@gmail.com", signUpForm.getPassword());
		resultActions = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnauthorized());
		result = resultActions.andReturn();
	}
	
	@Test
	@WithAnonymousUser
	void testLogin_WithBadCredentialsFourTimes_ThenExpectAccountTobeLocked() throws JsonProcessingException, Exception {
		LoginRequest loginRequest = new LoginRequest("bart@gmail.com", "JAmmm9$iijsjskskkkk");
		ResultActions resultActions = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnauthorized());
		MvcResult result = resultActions.andReturn();
		logger.info("status of bad creds test 1: " + result.getResponse().getContentAsString());

		resultActions = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnauthorized());
		result = resultActions.andReturn();
		logger.info("status of bad creds test 2: " + result.getResponse().getContentAsString());
		
		resultActions = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isUnauthorized());
			//	.andExpect(unauthenticated());

		result = resultActions.andReturn();
		logger.info("status of bad creds test 3: " + result.getResponse().getContentAsString());
	
		resultActions = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isUnauthorized())
				.andExpect(content().string("Login attempts have exceeded limit. Account is now blocked."));
                										
		result = resultActions.andReturn();
		logger.info("status of bad creds test 4: " + result.getResponse().getContentAsString());
	}
	
	@Test
	void login_WithValidCredentialsAndLogout_ThenSuccess() throws JsonProcessingException, Exception {
		final String testName = "login_WithValidCredentialsAndLogout_ThenSuccess";
		logger.info("Entered test: " + testName);
		TestUser user = TestUsers.getTestUser2();
		
		LoginRequest loginRequest = new LoginRequest(user.getUsername(),user.getPassword());
		logger.info("Try to login....");
		MvcResult result = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk()).andReturn();
		logger.info("Login succeeded.");
		HttpSession session = result.getRequest().getSession();
		assertNotNull(session);

		String token = testUtils.getTokenFromJSONResponse(result);
		assertNotNull(token);

		logger.info("Try to logout....");
		result = mockMvc.perform(post("/auth/logout")
				.cookie(authInfo.getCookie())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andReturn();	
		logger.info("Completed test: " + testName);

	}


	private String expectBadRequestException(UserDetailsRequest signUpForm) throws JsonProcessingException, Exception {
		ResultActions resultActions = mockMvc.perform(post("/auth/signUp").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpForm))).andExpect(status().isBadRequest());
		MvcResult result = resultActions.andReturn();
		String contentAsString = result.getResponse().getContentAsString();

		return contentAsString;
	}

}
