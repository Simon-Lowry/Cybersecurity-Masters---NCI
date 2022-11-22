package com.spfwproject.quotes.config;

import java.util.Arrays;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.spfwproject.quotes.constants.Roles;
import com.spfwproject.quotes.entities.RoleEntity;
import com.spfwproject.quotes.entities.UserEntity;

@TestConfiguration
public class SpringSecurityTestConfig {

    public UserDetailsService userDetailsService() {
    	/*
    	// regular user for testing
        UserEntity regularUser = new UserEntity("John Smith", "john@gmail.com", "somePassword",
        		"salt", false, "MyCity", "MyCountry");
        regularUser.setId(505L);
        RoleEntity role = new RoleEntity(Roles.USER.toString());
        regularUser.setRoles(Arrays.asList(role));
        
        // admin user for testing
    	UserEntity adminUser = new UserEntity("Jesus Christ", "jesus@godmail.com", "password", 
    			"salt", false, "MyCity", "MyCountry");    			
    	adminUser.setId(508L);
        role = new RoleEntity(Roles.ADMIN.toString());
        regularUser.setRoles(Arrays.asList(role));
        
        return new InMemoryUserDetailsManager(Arrays.asList(
        		regularUser, adminUser
        ));
        */
    	return null;
    }
}
