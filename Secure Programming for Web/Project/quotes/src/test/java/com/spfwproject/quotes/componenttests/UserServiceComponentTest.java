package com.spfwproject.quotes.componenttests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.services.UserServiceImpl;

@SpringBootTest
@Transactional
public class UserServiceComponentTest {
	@Autowired
	private UserServiceImpl userService;

	// (Long id, String name, String username, String password, String salt, boolean
	// accountLocked,
// 	String city, String country)
	@Test
	void testCreateUser() throws URISyntaxException {
		UserEntity userEntity = new UserEntity("Erik Tayloyr", "bob@gmail.com", "somePassword", "salt", false,
				"MyCity", "MyCountry");
		
		UserEntity user = userService.createUser(userEntity);

		assertNotNull(user.getId());
	}

}
