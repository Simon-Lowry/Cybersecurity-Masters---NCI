package com.spfwproject.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spfwproject.quotes.entities.QuoteEntity;

public interface QuoteRepository extends JpaRepository<QuoteEntity, Long>{

}
