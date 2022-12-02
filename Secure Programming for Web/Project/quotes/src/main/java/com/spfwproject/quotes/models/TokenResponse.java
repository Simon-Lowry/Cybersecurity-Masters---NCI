package com.spfwproject.quotes.models;

public class TokenResponse {
	private String token;
	private String tokenType;
	private Long userId;
	
	public TokenResponse(String token, String tokenType) {
		this.token = token;
		this.tokenType = tokenType;
	}
	
	public TokenResponse(String token, String tokenType, Long userId) {
		this.token = token;
		this.tokenType = tokenType;
		this.userId = userId;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getTokenType() {
		return tokenType;
	}
	
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
