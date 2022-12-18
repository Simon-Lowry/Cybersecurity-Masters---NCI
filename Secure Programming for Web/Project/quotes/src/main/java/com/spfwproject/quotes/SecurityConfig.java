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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.authentication.AuthenticationManager;

import com.spfwproject.quotes.constants.CookieNames;
import com.spfwproject.quotes.repositories.SessionRepository;
import com.spfwproject.quotes.services.AuthenticationServiceImpl;
import com.spfwproject.quotes.services.JWTTokenServiceImpl;
import com.spfwproject.quotes.services.SessionServiceImpl;
import com.spwproject.quotes.dbaccesslayer.UserDBAccess; 
import com.spwproject.quotes.dbaccesslayer.LoginAttemptsDBAccess;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
	public static final String LOGOUT_URI = "auth/logout";
	public static final String JSESSIONID = "JSESSIONID";
	public static final String LOGIN_URI = "login";
	public static final String USER_ROLE = "USER";
	
	@Bean
	public UserDBAccess userDetailsService() {
		return new UserDBAccess();
	}

	// csrf disabled since we're using jwt, meets the same purpose & makes that redundant.
	// session management: create new session on login for session fixation & only allow one session
	// httponly, secure flags are set in application.properties and 15 minutes session life. urlrewriting disabled too.
	// on logout, invalidate session and delete cookies
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// specify paths allowed based on roles, anonymous represents unauthenticated users
		http
		.cors()
		.and()
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
		
		http.csrf().disable()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
		.maximumSessions(1).and().sessionFixation().changeSessionId()
		.and().logout().logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URI))
		.deleteCookies(CookieNames.JSESSIONID.toString()).invalidateHttpSession(true)
		.clearAuthentication(true);
		
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
		authProvider.setPasswordEncoder(bcryptPasswordEncoder());

		return authProvider;
	}
	
	@Autowired
	JWTTokenServiceImpl tokenService;
	
	@Autowired
	SessionRepository sessionRepo;
		
	@Bean
	public JWTAuthenticationFilter authenticationTokenFilterBean() throws Exception {
	    return new JWTAuthenticationFilter(tokenService, userDetailsService(), new SessionServiceImpl(sessionRepo));
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider(userDetailsService()));
	}
	
	
	@Bean
	public PasswordEncoder bcryptPasswordEncoder() {
		 int strength = 10; // work factor of bcrypt
	    return new BCryptPasswordEncoder(strength, new SecureRandom());
	}
	
	@Bean
	public LoginAttemptsDBAccess loginAttemptsDBAccess() {
		return new LoginAttemptsDBAccess();
	}
	
}