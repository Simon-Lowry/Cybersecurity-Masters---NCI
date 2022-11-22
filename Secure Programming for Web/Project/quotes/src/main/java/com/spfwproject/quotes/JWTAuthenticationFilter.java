package com.spfwproject.quotes;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spfwproject.quotes.services.JWTTokenServiceImpl;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

	private final JWTTokenServiceImpl jwtTokenUtil;
    private final UserDBAccess userDBAccess;

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
        // Retrieve jwt token from header and ensure is bearer token
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(header) ||  !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        final String token = header.split(" ")[1].trim();

        // Get user details
        UserDetails userDetails = userDBAccess
            .loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
            
        // Parse token and perform validation
        if (!jwtTokenUtil.validateToken(token, userDetails)) {
            chain.doFilter(request, response);
            return;
        }

        // add token to  security context
        UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails.getAuthorities()
            );

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

	  
	  

}
