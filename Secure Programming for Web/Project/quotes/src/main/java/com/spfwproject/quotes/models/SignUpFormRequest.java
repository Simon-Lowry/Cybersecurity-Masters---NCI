package com.spfwproject.quotes.models;

import com.spfwproject.quotes.entities.UserEntity;

public class SignUpFormRequest {
	private String name;
	private String username;
	private String password;
	private String passwordRepeated;
	private String city;
	private String country;

	public SignUpFormRequest(String name, String username, String password, String passwordRepeated, String city,
			String country) {
		this.name = name;
		this.username = username;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public UserEntity convertSignUpFormToUserEntity(String password, String salt) {
		return new UserEntity(null, getName(), getUsername(), password, salt, false, getCity(), getCountry());
	}

	@Override
	public String toString() {
		return "SignUpForm [name=" + name + ", username=" + username + ", password=" + password + ", passwordRepeated="
				+ passwordRepeated + ", city=" + city + ", country=" + country + "]";
	}
}
