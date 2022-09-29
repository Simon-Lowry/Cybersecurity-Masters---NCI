package com.spfwproject.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spfwproject.quotes.entities.QuoteDBO;

public interface QuoteRepository extends JpaRepository<QuoteDBO, Long>{

}
