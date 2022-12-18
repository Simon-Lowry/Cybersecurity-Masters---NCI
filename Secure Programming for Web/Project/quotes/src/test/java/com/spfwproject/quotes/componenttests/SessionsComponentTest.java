package com.spfwproject.quotes.componenttests;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.SecurityConfig;
import com.spfwproject.quotes.exceptions.InvalidSessionException;
import com.spfwproject.quotes.exceptions.MalformedTokenException;
import com.spfwproject.quotes.models.LoginRequest;
import com.spfwproject.quotes.models.UserDetailsRequest;
import com.spfwproject.quotes.utils.TestAuthInfo;
import com.spfwproject.quotes.utils.TestUtils;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionsComponentTest {
	private Logger logger = LoggerFactory.getLogger(PrivilegeEscalationAndRoutingComponentTest.class);

	@Autowired
	UserDBAccess userDetailsService;
    
    @Autowired
	private TestUtils testUtils;
  
    @Autowired
	private ObjectMapper objectMapper;
    
    private final String JSESSIONID = "JSESSIONID";
        
	private static UserDetailsRequest signUpForm;
	
    @Autowired
	private MockMvc mockMvc;
    
	private TestAuthInfo authInfo;
	
	String token;
		
	@BeforeAll
	void setup() throws JsonProcessingException, Exception {
		authInfo = testUtils.loginAndReturnAuthInfo();
	}
	
	@Test
	void givenAnAutenticatedUserWithNoSession_WhenTheUserMakesARequestForLoggedInFunctionality_ThenASessionInvalidExceptionIsThrown() 
			throws JsonProcessingException, Exception {
		final String methodName = "givenAnAutenticatedUserWithNoSession_WhenTheUserMakesARequestForLoggedInFunctionality_ThenASessionInvalidExceptionIsThrown";
				
		assertThrows(InvalidSessionException.class, () ->{ 
			mockMvc.perform(get("/users/506").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isUnauthorized()).andReturn();
		});
		logger.info("Completed test " + methodName);
	}
	
	@Test
	void givenAnAutenticatedUserWithExpiredSession_WhenTheUserMakesARequestForLoggedInFunctionality_ThenASessionInvalidExceptionIsThrown() 
			throws JsonProcessingException, Exception {
		final String methodName = "givenAnAutenticatedUserWithNoSession_WhenTheUserMakesARequestForLoggedInFunctionality_ThenASessionInvalidExceptionIsThrown";
			
		Cookie expiredSessionCookie = new Cookie(JSESSIONID, "aaaafb2e77d.47a0479900504cb3ab4a1f626d174d2d");
		assertThrows(InvalidSessionException.class, () ->{ 
			mockMvc.perform(get("/users/506")
					.cookie(expiredSessionCookie)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isUnauthorized()).andReturn();
		});
		logger.info("Completed test " + methodName);
	}
	
}
