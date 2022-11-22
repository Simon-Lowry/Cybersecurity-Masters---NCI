package com.spfwproject.quotes.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spfwproject.quotes.entities.LoginAttemptsEntity;

public interface LoginAttemptsRepository extends JpaRepository<LoginAttemptsEntity, Integer> {
	Optional<LoginAttemptsEntity> findAttemptsByUserId(Long userId);
}
