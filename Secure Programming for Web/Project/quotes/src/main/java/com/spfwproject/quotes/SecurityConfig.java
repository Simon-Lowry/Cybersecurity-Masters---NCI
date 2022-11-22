package com.spfwproject.quotes;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;


import com.spfwproject.quotes.services.AuthenticationServiceImpl;
import com.spfwproject.quotes.services.JWTTokenServiceImpl;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
	public static final String LOGOUT_URI = "/logout";
	public static final String JSESSIONID = "JSESSIONID";
	public static final String LOGIN_URI = "/login";
	public static final String ADMIN_SUCCESS_URL = "TBD/admin/dashboard";
	public static final String USER_ROLE = "USER";
	public static final String USER_SUCCESS_URL = "TBD/user/dashboard";


	
	/*
	 * @Autowired BCryptPasswordEncoder crypt;
	 */

	@Bean
	public UserDBAccess userDetailsService() {
		return new UserDBAccess();
	}

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.httpBasic().and().cors().and().csrf().disable().sessionManagement().invalidSessionUrl(LOGIN_URI)
				.maximumSessions(1).and().sessionFixation().newSession().and().logout().logoutUrl(LOGOUT_URI)
				.logoutSuccessUrl(LOGIN_URI).deleteCookies(JSESSIONID);
		
		http.authorizeRequests()
		.mvcMatchers("/admin/**").hasRole("ADMIN")
		.mvcMatchers("/auth/logout").hasRole("USER")
		.mvcMatchers("/quotes/**").hasRole("USER")
		.mvcMatchers("/users/**").hasAnyRole("USER")
		.mvcMatchers("/auth/signUp").hasAnyRole("ANONYMOUS")
		.mvcMatchers("/auth/login").hasAnyRole("ANONYMOUS")
		.anyRequest().denyAll(); 
		
		 // Add JWT token filter
        http.addFilterBefore(
        	authenticationTokenFilterBean(),
            UsernamePasswordAuthenticationFilter.class
        );
		return http.build();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider(UserDBAccess userDetailsService) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(encoder());

		return authProvider;
	}
	
	@Autowired
	JWTTokenServiceImpl tokenService;
		
	@Bean
	public JWTAuthenticationFilter authenticationTokenFilterBean() throws Exception {
	    return new JWTAuthenticationFilter(tokenService, userDetailsService());
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider(userDetailsService()));
	}
	
	@Bean
	public PasswordEncoder encoder() {
		 int strength = 10; // work factor of bcrypt
	    return new BCryptPasswordEncoder(strength, new SecureRandom());
	}
	
}