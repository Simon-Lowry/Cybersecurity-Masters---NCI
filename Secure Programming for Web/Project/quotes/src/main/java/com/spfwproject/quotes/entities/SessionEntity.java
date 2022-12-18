package com.spfwproject.quotes.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Sessions")
public class SessionEntity {
	@Id
	@NotNull
	@Column(name = "user_id", unique=true)
	private Long userId;
	
	@NotNull
	private String sessionId;
	
	@NotNull
	private Long sessionCreationTime;
	
	public SessionEntity() {};
	
	public SessionEntity(@NotNull Long userId, @NotNull String sessionId, @NotNull Long sessionCreationTime) {
		this.userId = userId;
		this.sessionId = sessionId;
		this.sessionCreationTime = sessionCreationTime;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getSessionCreationTime() {
		return sessionCreationTime;
	}

	public void setSessionCreationTime(Long sessionCreationTime) {
		this.sessionCreationTime = sessionCreationTime;
	}

}
