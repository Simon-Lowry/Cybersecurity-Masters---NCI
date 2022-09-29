package com.spfwproject.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spfwproject.quotes.entities.UserDBO;

public interface UserRepository extends JpaRepository<UserDBO, Long> {
	
}