package com.spfwproject.quotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.spfwproject.quotes.services.AuthenticationService;
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

	@Autowired
	AuthenticationService authService;

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

		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider authProvider(UserDBAccess userDetailsService) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		return authProvider;
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider(userDetailsService()));
	}

	/*
	 * @Bean protected void configure(AuthenticationManagerBuilder auth) {
	 * auth.authenticationProvider(authService); }
	 */

	/*
	 * 
	 * @Override public void configure(AuthenticationManagerBuilder
	 * authenticationManagerBuilder) throws Exception { authenticationManagerBuilder
	 * .userDetailsService(customUserDetailsService)
	 * .passwordEncoder(passwordEncoder()); }
	 * 
	 * @Bean(BeanIds.AUTHENTICATION_MANAGER)
	 * 
	 * @Override public AuthenticationManager authenticationManagerBean() throws
	 * Exception { return super.authenticationManagerBean(); }
	 * 
	 */

}