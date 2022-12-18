package com.spfwproject.quotes.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.entities.SessionEntity;
import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.integrationtests.AuthenticationControllerIntegrationTest;
import com.spfwproject.quotes.interfaces.JWTTokenService;
import com.spfwproject.quotes.models.LoginRequest;
import com.spfwproject.quotes.repositories.SessionRepository;
import com.spfwproject.quotes.services.UserServiceImpl;

@Component
public class TestUtils {
	@Autowired
	private JWTTokenService tokenService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SessionRepository sessionRepository;
	
	private static Logger logger = LoggerFactory.getLogger(TestUtils.class);

	@Autowired
    private MockMvc mockMvc;
	
	public TestUtils (JWTTokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	public String generateAdminToken() {
		ArrayList<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
        authoritiesList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		String token = tokenService.generateToken("jesus@godmail.com", authoritiesList);
		return token;
	}
	
	public String generateUserToken() {
		ArrayList<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
        authoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));
		String token = tokenService.generateToken("simon@gmail.com", authoritiesList);
		return token;
	}
	
	public String generateUserToken(String username) {
		ArrayList<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
        authoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));
		String token = tokenService.generateToken(username, authoritiesList);
		return token;
	}
	
	public String loginAndGetTokenFromResponse() throws JsonProcessingException, Exception {
		LoginRequest loginRequest = new LoginRequest("simon@gmail.com","(Drezden90)");
		MvcResult result = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk()).andReturn();

		String token = getTokenFromJSONResponse(result);
		assertNotNull(token);
		return token;
	}
	
	public String getTokenFromJSONResponse(MvcResult result) {
		try {
			MockHttpServletResponse response = result.getResponse();
			String responseString = response.getContentAsString();
		     JSONObject jsonObject = new JSONObject(responseString);
		     logger.info("Response: " + jsonObject );

		     String token = jsonObject.getString("token");
		    
		     logger.info("TOken: " + token);
		     return token;
		}catch (JSONException | UnsupportedEncodingException err){
		     logger.error("Error", err.toString());
		     return null;
		} 
	}
	
	public boolean setTestSecurityContextWithAuthorisation(String username, String password) {
		logger.info("Test security context: " + TestSecurityContextHolder.getContext());	
		
		if (TestSecurityContextHolder.getContext() != null ) {
			logger.info("Test security context auth: " + TestSecurityContextHolder.getContext().getAuthentication());
			
			SecurityContext securityContext = TestSecurityContextHolder.getContext();
			
			if (securityContext.getAuthentication() == null) {
				
				UserEntity user = userService.getUserByUsername(username);
				logger.info("User succesfully retrieved: " + user.toString());
				ArrayList<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
		        authoritiesList.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
				
		        UsernamePasswordAuthenticationToken authentication =
		        		new UsernamePasswordAuthenticationToken(user, password, authoritiesList);
				securityContext.setAuthentication(authentication);
			}
			SecurityContextHolder.setContext(securityContext);
		}
		return true;
		
	}
	

	public TestAuthInfo loginAndReturnAuthInfo() throws JsonProcessingException, Exception {
		return loginAndReturnAuthInfo(TestUsers.getTestUser1().getUsername(), TestUsers.getTestUser1().getPassword());
	}
	
	
	public TestAuthInfo loginAndReturnAuthInfo(String username, String password) throws JsonProcessingException, Exception {
		LoginRequest loginRequest = new LoginRequest(username,password);

		MvcResult result = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk()).andReturn();
		logger.info("Login succeeded.");
		MockHttpServletRequest request = result.getRequest();
		HttpSession session = result.getRequest().getSession();
		Cookie sessionCookie = new Cookie("JSESSIONID", "fb2e77d.47a0479900504cb3ab4a1f626d174d2d");
		
		String token = getTokenFromJSONResponse(result);
		assertNotNull(token);
		
		TestAuthInfo authInfo = new TestAuthInfo(session, token, sessionCookie);
		return authInfo;
	}
	
	public void resetPasswordForTestUser(Long id) {
		String password = "$2a$10$cd5f7kegrb7XtNWZ8V2QreUH5jLO2XKzJllXwwatgYBH6mHvjku.K";
		
		userService.changePassword(id, password);
	}
	
	public void resetTestSessionCreationTime() {
		Optional<SessionEntity> optionalExistingSession = sessionRepository.findById(999L);
		
		if (optionalExistingSession != null) {
			SessionEntity existingSession = optionalExistingSession.get();
			existingSession.setSessionCreationTime(9671254073770L);
     		sessionRepository.save(existingSession);		
		} 

	}

}
