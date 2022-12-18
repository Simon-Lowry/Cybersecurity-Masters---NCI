package com.spfwproject.quotes.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

public class TestAuthInfo {
	private HttpSession session;
	private String token;
	private Cookie cookie;
	
	public TestAuthInfo() {}
	
	public TestAuthInfo(HttpSession session, String token, Cookie cookie) {
		this.session = session;
		this.token = token;
		this.cookie = cookie;
	}

	public HttpSession getSession() {
		return session;
	}
	
	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	public Cookie getCookie() {
		return cookie;
	}

	public void setCookies(Cookie cookie) {
		this.cookie = cookie;
	}

}
