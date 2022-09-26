package com.spfwproject.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spfwproject.quotes.entities.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Long>{

}
