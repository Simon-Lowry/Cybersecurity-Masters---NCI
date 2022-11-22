package com.spfwproject.quotes.entities;

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
	private Long id;

	@NotNull
	private Long userId;
	@NotEmpty
	private String quoteText;
	@NotEmpty
	private String quotePrivacySetting;
	@NotEmpty
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
		QuotePrivacySettings role = QuotePrivacySettings.valueOf(quoteText);
		this.quoteText = role.toString();
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
