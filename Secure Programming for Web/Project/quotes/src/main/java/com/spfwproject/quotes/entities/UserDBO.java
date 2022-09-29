package com.spfwproject.quotes.entities;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.spfwproject.quotes.models.UserResponse;

@Entity
@Table(name = "Users")
public class UserDBO {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private String email;
	private String city;
	private String country;
	private byte[] hashedPassword;
	private byte[] salt;
	
	@Column(name = "account_locked")
	private boolean accountLocked;

	public UserDBO() {};

	public UserDBO(Long id, String name, String email, byte[] hashedPassword, byte[] salt, boolean accountLocked, String city, String country ) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
		this.accountLocked = accountLocked;
		this.city = city;
		this.country = country;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public UserResponse convertUserEntityToUserResponse() {
		return new UserResponse(getId(), getName(), getEmail(), getCity(), getCountry());
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", city=" + city + ", country=" + country
				+ ", hashedPassword=" + Arrays.toString(hashedPassword) + ", salt=" + Arrays.toString(salt)
				+ ", accountLocked=" + accountLocked + "]";
	}
}
