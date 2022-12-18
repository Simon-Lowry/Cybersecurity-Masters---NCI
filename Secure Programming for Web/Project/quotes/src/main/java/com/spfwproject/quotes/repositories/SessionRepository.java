package com.spfwproject.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spfwproject.quotes.entities.SessionEntity;

public interface SessionRepository extends JpaRepository<SessionEntity, Long>{
	
	@Query(value = "Select s FROM SessionEntity s where s.sessionId=?1")
	SessionEntity getSessionBySessionId(String sessionId);
}
