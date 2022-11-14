package com.spfwproject.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spfwproject.quotes.entities.UserEntity;
import com.spfwproject.quotes.interfaces.IUserRepository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, IUserRepository {
	UserEntity findUserByUsername(String username);

}