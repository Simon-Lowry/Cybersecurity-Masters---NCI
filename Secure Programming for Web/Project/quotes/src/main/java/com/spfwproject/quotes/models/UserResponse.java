package com.spfwproject.quotes.models;

public class UserResponse {
	private Long id;
	private String name;
	private String email;
	private String city;
	private String country;
	
	public UserResponse(Long id, String name, String email, String city, String country) {
		this.id = id;
		this.name = name;
		this.email = email;
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

	@Override
	public String toString() {
		return "UserResponse [id=" + id + ", name=" + name + ", email=" + email + ", city=" + city + ", country="
				+ country + "]";
	}
}
