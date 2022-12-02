package com.spfwproject.quotes.utils;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.spfwproject.quotes.interfaces.JWTTokenService;

@Component
public class TestUtils {
	@Autowired
	private JWTTokenService tokenService;
	
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

}
