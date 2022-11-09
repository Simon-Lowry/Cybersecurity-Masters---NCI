package com.spfwproject.quotes.componenttests.services;

import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.services.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceComponentTest {
	@Autowired
	private UserService userService;
	
	@Test
	void testCreateUser() throws URISyntaxException {
		UserEntity userEntity = new UserEntity(505L, "Jacob Brady", "jacob@gmail.com", null,
				null, false, "MyCity", "MyCountry");
		
		UserEntity user = userService.createUser(userEntity);
		
		assertEquals(user.getId(), 505L);
	}

}
