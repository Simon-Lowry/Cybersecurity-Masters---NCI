package com.spfwproject.quotes.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spfwproject.quotes.entities.QuoteEntity;

public interface QuoteRepository extends JpaRepository<QuoteEntity, Long>{
	
	@Query(value = "Select q FROM QuoteEntity q where q.userId=?1")
	List<QuoteEntity> findAllQuotesForAGivenUser(Long userId);

}
