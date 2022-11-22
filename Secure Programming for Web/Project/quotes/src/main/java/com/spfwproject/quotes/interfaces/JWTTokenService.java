package com.spfwproject.quotes.interfaces;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JWTTokenService {

	public String getUsernameFromToken(String token);

	public Date getExpirationDateFromToken(String token);

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

	public String generateToken(Authentication authentication);

	public Boolean validateToken(String token, UserDetails userDetails);

}
