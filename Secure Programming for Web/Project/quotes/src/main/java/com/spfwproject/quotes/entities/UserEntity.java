package com.spfwproject.quotes.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.spfwproject.quotes.models.UserResponse;

@Entity
@Table(name = "Users")
public class UserEntity extends User {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@Column(name = "username")
	private String username;

	private String city;
	private String country;

	@Column(name = "hashed_password")
	private String password;
	private String salt;

	@Column(name = "account_locked")
	private boolean accountLocked;
	
	@ManyToMany 
    @JoinTable( 
        name = "users_roles", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Collection<RoleEntity> roles;

	public UserEntity() {
		super("temp", "temp", true, false, false, false, new ArrayList<GrantedAuthority>());
	}

	public UserEntity(String name, String username, String password, String salt, boolean accountLocked,
			String city, String country) {
		super(username, password, true, false, false, !accountLocked, new ArrayList<GrantedAuthority>());
		this.name = name;
		this.username = username;
		this.password = password;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String hashedPassword) {
		this.password = hashedPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
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
	
	
	public Collection<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleEntity> roles) {
		this.roles = roles;
	}

	public UserResponse convertUserEntityToUserResponse() {
		return new UserResponse(getId(), getName(), getUsername(), getCity(), getCountry());
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username + ", city=" + city + ", country="
				+ country + ", hashedPassword=" + password + ", salt=" + salt + ", accountLocked=" + accountLocked
				+ "]";
	}
}
