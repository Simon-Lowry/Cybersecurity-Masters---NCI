package com.spfwproject.quotes.utils;

public class TestUser {
	private final String username;
	private final String password;
	// private final String hashedPassword;
	private final Long userId;
	
	public TestUser(String username, String password, Long userId) {
		this.username = username;
		this.password = password;
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Long getUserId() {
		return userId;
	}

}
