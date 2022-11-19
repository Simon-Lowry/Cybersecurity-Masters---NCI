package com.spfwproject.quotes.models;

import com.spfwproject.quotes.entities.QuoteEntity;

public class QuoteResponse {
	private Long quoteId;
	private Long userId;
	private String quoteText;
	private String quotePrivacySetting;
	
	public QuoteResponse(Long quoteId, Long userId, String quoteText, String quotePrivacySetting) {
		this.quoteId = quoteId;
		this.userId = userId;
		this.quoteText = quoteText;
		this.quotePrivacySetting = quotePrivacySetting;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
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
	
	public static QuoteResponse convertQuoteEntityToQuoteResponse(QuoteEntity quoteEntity) {
		return new QuoteResponse(quoteEntity.getId(), quoteEntity.getUserId(), quoteEntity.getQuoteText(),
				quoteEntity.getQuotePrivacySetting());
	}

	@Override
	public String toString() {
		return "QuoteResponse [userId=" + userId + ", quoteText=" + quoteText + ", quotePrivacySetting="
				+ quotePrivacySetting + "]";
	}

}
