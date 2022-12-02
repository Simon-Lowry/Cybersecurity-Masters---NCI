package com.spfwproject.quotes.models;

public class UserResponse {
	private Long id;
	private String name;
	private String email;
	private String city;
	private String country;
	private String token;
	private String tokenType;
	
	public UserResponse(Long id, String name, String email, String city, String country) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.city = city;
		this.country = country;
	}
	
	public UserResponse(Long id, String name, String email, String city, String country, 
			String token, String tokenType) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.city = city;
		this.country = country;
		this.token = token;
		this.tokenType = tokenType;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@Override
	public String toString() {
		return "UserResponse [id=" + id + ", name=" + name + ", email=" + email + ", city=" + city + ", country="
				+ country + "]";
	}
}
