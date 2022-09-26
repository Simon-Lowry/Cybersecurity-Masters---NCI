package com.spfwproject.quotes.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private String email;
	private byte[] hashedPassword;
	private byte[] salt;
	
	@Column(name = "account_locked")
	private boolean accountLocked;

	public User() {};

	public User(Long id, String name, String email, byte[] hashedPassword, byte[] salt, boolean accountLocked ) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
		this.accountLocked = accountLocked;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(byte[] hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
}
