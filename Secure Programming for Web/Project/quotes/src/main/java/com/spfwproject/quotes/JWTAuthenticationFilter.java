package com.spfwproject.quotes;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.exceptions.InvalidTokenException;
import com.spfwproject.quotes.exceptions.MalformedTokenException;
import com.spfwproject.quotes.services.AuthenticationServiceImpl;
import com.spfwproject.quotes.services.JWTTokenServiceImpl;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
	private Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	private final JWTTokenServiceImpl jwtTokenUtil;
    private final UserDBAccess userDBAccess;
    private final String LOGIN_URL = "/auth/login";
    private final String SIGNUP_URL = "/auth/signUp";
    
    public JWTAuthenticationFilter(JWTTokenServiceImpl jwtTokenUtil,
    		UserDBAccess userRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDBAccess = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
    	logger.info("Entered JWT Filter for request.");
    	String pathInfo = request.getRequestURI();
    	logger.info("Path requested for request: " + pathInfo);
    	ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

    	// unauthenticated users urls should be allowed through and not require token validation
    	if((pathInfo != null) && (pathInfo.equals(LOGIN_URL) || pathInfo.equals(SIGNUP_URL))) {
            logger.info("Request is for either login or signup, skipping jwt filter.");
            chain.doFilter(wrappedRequest, response);
            return;
        }
    	
        // Retrieve jwt token from header and ensure is bearer token	
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || header.isEmpty() ||  !header.startsWith("Bearer ")) {
        	logger.info("Issue with token structure or is empty.");
        	throw new MalformedTokenException();
        }
        final String token = header.split(" ")[1].trim();

        // Get user details
        logger.info("Pre-get user with username from token");
    	UserEntity userDetails = (UserEntity) userDBAccess
            .loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));    	
            
        // Parse token and perform validation
        if (!jwtTokenUtil.validateToken(token, userDetails)) {
        	logger.info("Token invalid, requested filtered.");
        	throw new InvalidTokenException();
        }
    	logger.info("Post validate token, user role: " + userDetails.getRole().getName());

        // add token to  security context
        ArrayList<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
        authoritiesList.add(new SimpleGrantedAuthority("ROLE_" +userDetails.getRole().getName()));

        UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                authoritiesList
            );

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(wrappedRequest, response);
    	logger.info("Succesfully validated jwt token filter. Leaving jwt token filter");
    }

	  
	  

}
