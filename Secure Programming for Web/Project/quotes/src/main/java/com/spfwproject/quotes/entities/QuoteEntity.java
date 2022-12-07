package com.spfwproject.quotes.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.spfwproject.quotes.constants.QuotePrivacySettings;
import com.spfwproject.quotes.models.QuoteRequest;

@Entity
@Table(name = "Quotes")
public class QuoteEntity {
	@Id
	@GeneratedValue
	@NotNull
	@Column(name="quote_id")
	private Long id;

	@NotNull
	@Column(name="user_id")
	private Long userId;
	
	@NotEmpty
	@Column(name="quote_text")
	private String quoteText;
	
	@NotEmpty
	@Column(name="quote_privacy_setting")
	private String quotePrivacySetting;
	
	@NotEmpty
	@Column(name="quote_author")
	private String quoteAuthor;

	public QuoteEntity() {
	};

	public QuoteEntity(Long userId, String quoteText, String quotePrivacySetting, String quoteAuthor) {
		this.userId = userId;
		this.quoteText = quoteText;
		this.quotePrivacySetting = quotePrivacySetting;
		this.quoteAuthor = quoteAuthor;
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
		//QuotePrivacySettings setting = QuotePrivacySettings.valueOf(quoteText);
		this.quotePrivacySetting = quotePrivacySetting;
	}

	public String getQuoteAuthor() {
		return quoteAuthor;
	}

	public void setQuoteAuthor(String quoteAuthor) {
		this.quoteAuthor = quoteAuthor;
	}

	public static QuoteEntity convertQuoteRequestToQuoteEntity(QuoteRequest quoteRequest) {
		return new QuoteEntity(quoteRequest.getUserId(), quoteRequest.getQuoteText(),
				quoteRequest.getQuotePrivacySetting(), quoteRequest.getQuoteAuthor());
	}

	@Override
	public String toString() {
		return "QuoteEntity [id=" + id + ", userId=" + userId + ", quoteText=" + quoteText + ", quotePrivacySetting="
				+ quotePrivacySetting + ", quoteAuthor=" + quoteAuthor + "]";
	}

}
