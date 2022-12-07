package com.spfwproject.quotes;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationManager;


import com.spfwproject.quotes.services.AuthenticationServiceImpl;
import com.spfwproject.quotes.services.JWTTokenServiceImpl;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess; 
import com.spwproject.quotes.dbaccesslayer.LoginAttemptsDBAccess;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
	public static final String LOGOUT_URI = "auth/logout";
	public static final String JSESSIONID = "JSESSIONID";
	public static final String LOGIN_URI = "/login";
	public static final String USER_ROLE = "USER";
	
	/*
	 * @Autowired BCryptPasswordEncoder crypt;
	 */

	@Bean
	public UserDBAccess userDetailsService() {
		return new UserDBAccess();
	}

	// csrf disabled since we're using jwt, making that redundant.
	// session management: create new session on login for session fixation & only allow one session
	// httponly, secure flags are set in application.properties and 15 minutes session life. urlrewriting disabled too.
	// on logout, invalidate session and delete cookies
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.sessionManagement().invalidSessionUrl(LOGIN_URI)
				.maximumSessions(1).and().sessionFixation().newSession()
				.and().logout().logoutUrl(LOGOUT_URI).deleteCookies(JSESSIONID).invalidateHttpSession(true);
		
		// specify paths allowed based on roles, anonymous represents unauthenticated users
		http
		.httpBasic()
	    .authenticationEntryPoint(new NoPopupBasicAuthenticationEntryPoint())
	    .and()
		.authorizeRequests()
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
        
        http.cors();
		return http.build();
	}
	

	// Cross origin allowlist limited to the UI of the application and specific headers and http verbs relevant
	// to the application
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();	   
	    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
	    configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
	    configuration.setExposedHeaders(Arrays.asList("Authorization", "content-type"));
	    configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
	    configuration.setAllowCredentials(true);	 

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
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
	
	@Bean
	public LoginAttemptsDBAccess loginAttemptsDBAccess() {
		return new LoginAttemptsDBAccess();
	}
	
}