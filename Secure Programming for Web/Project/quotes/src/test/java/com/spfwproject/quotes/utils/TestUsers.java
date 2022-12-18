package com.spfwproject.quotes.utils;

import com.spfwproject.quotes.entities.UserEntity;

public class TestUsers {
	public static TestUser getTestUser1() { 
		return new TestUser("simon@gmail.com", "K@ExFUks4l9my%d", 506L);
		// hashed: $2a$10$cd5f7kegrb7XtNWZ8V2QreUH5jLO2XKzJllXwwatgYBH6mHvjku.K
	}
	
	public static TestUser getTestUser2() { 
		return new TestUser("testUser@gmail.com", "K@ExFUks4l9my%d", 510L);
		// hashed: $2a$10$cd5f7kegrb7XtNWZ8V2QreUH5jLO2XKzJllXwwatgYBH6mHvjku.K
	}
	
	public static TestUser getTestUser3() { 
		return new TestUser("testUser2@gmail.com", "K@ExFUks4l9my%d", 511L);
		// hashed: $2a$10$cd5f7kegrb7XtNWZ8V2QreUH5jLO2XKzJllXwwatgYBH6mHvjku.K
	}
	
	public static TestUser getAdminTestUser1() {
		return new TestUser("jacob@gmail.com", 
				"$10$UZHkbna/QIly9S6PZ./CY.8cWiSPFo.T6KBLyeTeAzrrSE2giIJHS", 509L);

	}

}
