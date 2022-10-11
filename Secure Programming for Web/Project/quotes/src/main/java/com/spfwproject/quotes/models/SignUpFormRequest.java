package com.spfwproject.quotes.models;

import com.spfwproject.quotes.entities.UserEntity;

public class SignUpFormRequest {
	private String name;
	private String email;
	private String password;
	private String passwordRepeated;
	private String city;
	private String country;
	
	public SignUpFormRequest(String name, String email, String password, String passwordRepeated, String city,
			String country) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.passwordRepeated = passwordRepeated;
		this.city = city;
		this.country = country;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordRepeated() {
		return passwordRepeated;
	}

	public void setPasswordRepeated(String passwordRepeated) {
		this.passwordRepeated = passwordRepeated;
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
	
	public UserEntity convertSignUpFormToUserEntity() {
		return new UserEntity(null, getName(), getEmail(), null, null, false, getCity(), getCountry());	
	}

	@Override
	public String toString() {
		return "SignUpForm [name=" + name + ", email=" + email + ", password=" + password + ", passwordRepeated="
				+ passwordRepeated + ", city=" + city + ", country=" + country + "]";
	}
}
