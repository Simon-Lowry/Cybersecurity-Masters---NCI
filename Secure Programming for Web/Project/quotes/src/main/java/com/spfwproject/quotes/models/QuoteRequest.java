package com.spfwproject.quotes.models;

public class QuoteRequest {
	private Long userId;
	private String quoteText;
	private String quotePrivacySetting;
	private String quoteAuthor;
	private Long quoteId; 
	
	public QuoteRequest() {}

	public QuoteRequest(Long userId, String quoteText, String quotePrivacySetting, String author) {
		this.userId = userId;
		this.quoteText = quoteText;
		this.quotePrivacySetting = quotePrivacySetting;
		this.quoteAuthor = author;
	}
	
	public QuoteRequest(Long quoteId, Long userId, String quoteText, String quotePrivacySetting, String author) {
		this.quoteId = quoteId;
		this.userId = userId;
		this.quoteText = quoteText;
		this.quotePrivacySetting = quotePrivacySetting;
		this.quoteAuthor = author;
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

	public String getQuoteAuthor() {
		return quoteAuthor;
	}

	public void setQuoteAuthor(String quoteAuthor) {
		this.quoteAuthor = quoteAuthor;
	}

	public Long getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Long quoteId) {
		this.quoteId = quoteId;
	}

	@Override
	public String toString() {
		return "QuoteRequest [userId=" + userId + ", quoteText=" + quoteText + ", quotePrivacySetting="
				+ quotePrivacySetting + ", quoteAuthor=" + quoteAuthor + ", quoteId=" + quoteId + "]";
	}

}
