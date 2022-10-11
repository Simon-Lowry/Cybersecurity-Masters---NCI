package com.spfwproject.quotes.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Quotes")
public class QuoteEntity {
	@Id
    @GeneratedValue
    private Long id;
    
	private Long userId;
	private String quoteText;
	private String quotePrivacySetting;
    
    public QuoteEntity() {};
    
    public QuoteEntity(Long id, Long userId, String quoteText, String quotePrivacySetting) {
		this.id = id;
		this.userId = userId;
		this.quoteText = quoteText;
		this.quotePrivacySetting = quotePrivacySetting;
	}
    
    public Long getId() {
		return id;
	}
        
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public String getQuoteText() {
		return quoteText;
	}
	
	public void setQuoteText(String quoteText) {
		this.quoteText = quoteText;
	}

	public String getQuotePrivacySetting() {
		return quotePrivacySetting;
	}

	public void setQuotePrivacySetting(String quotePrivacySetting) {
		this.quotePrivacySetting = quotePrivacySetting;
	}

}
