package com.spfwproject.quotes.componenttests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.services.UserService;

@SpringBootTest
public class UserServiceComponentTest {
	@Autowired
	private UserService userService;

	// (Long id, String name, String username, String password, String salt, boolean
	// accountLocked,
// 	String city, String country)
	@Test
	void testCreateUser() throws URISyntaxException {
		UserEntity userEntity = new UserEntity("Jacob Brady", "jacob@gmail.com", "somePassword", "salt", false,
				"MyCity", "MyCountry");
		
		UserEntity user = userService.createUser(userEntity);

		assertEquals(user.getId(), 1L);
	}

}
