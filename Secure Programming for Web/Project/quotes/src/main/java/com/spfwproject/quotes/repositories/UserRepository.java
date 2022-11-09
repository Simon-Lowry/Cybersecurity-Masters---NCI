package com.spfwproject.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.interfaces.IUserRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>, IUserRepository {
	
}