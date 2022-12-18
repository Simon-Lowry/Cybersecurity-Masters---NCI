package com.spfwproject.quotes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.spfwproject.quotes.constants.CookieNames;

import org.springframework.core.Ordered;

@Configuration(proxyBeanMethods = false)
public class WebConfig{
	  
	  @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {

	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	            	registry.addMapping("/**")
	    	        .allowedOrigins("https://localhost:3000", "https://127.0.0.1:3000", "https://quotes.app.v1.local:3000")
	    	        .allowedMethods("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS")
	    	        .exposedHeaders("Authorization", "content-type")
	    	        .allowedHeaders("Authorization", "content-type")
	    	        .allowCredentials(true);
	            }

	        };
	    }
	  
	  /*
	  @Bean
		public CookieSerializer cookieSerializer() {
			DefaultCookieSerializer serializer = new DefaultCookieSerializer();
			serializer.setCookieName(CookieNames.JSESSIONID.toString()); 
			serializer.setCookiePath("/"); 
			serializer.setSameSite("none");
			serializer.setUseHttpOnlyCookie(true);
			serializer.setUseSecureCookie(true);

			serializer.setDomainName("quotes.app.v1.local"); 
			return serializer;
		}
		*/

}
