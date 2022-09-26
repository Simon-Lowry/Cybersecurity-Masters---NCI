package com.spfwproject.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spfwproject.quotes.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
}