package com.spfwproject.quotes.integrationtests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spfwproject.quotes.models.UserDetailsRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class QuotesControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private Logger logger = LoggerFactory.getLogger(QuotesControllerIntegrationTest.class);

}
