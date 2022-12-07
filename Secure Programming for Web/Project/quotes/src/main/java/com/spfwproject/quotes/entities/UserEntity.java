package com.spfwproject.quotes.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.spfwproject.quotes.constants.Roles;
import com.spfwproject.quotes.exceptions.PrivilegeEscalationException;
import com.spfwproject.quotes.models.UserResponse;

@Entity
@Table(name = "Users")
public class UserEntity extends User {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@NotNull
	@Column(name = "user_id")
	private Long id;

	@NotEmpty
	private String name;

	@Column(name = "username", unique=true)
	@NotEmpty
	private String username;

	@NotEmpty
	private String city;
	@NotEmpty
	private String country;

	@Column(name = "hashed_password")
	@NotEmpty
	private String password;
	private String salt;

	@Column(name = "account_locked")
	private boolean accountLocked;

	@OneToOne()
	@JoinTable(name = "User_Role", 
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true), 
		inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
	)
	private RoleEntity role;

	/*
	@ManyToMany
	@JoinTable(name = "users_quotes", 
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), 
		inverseJoinColumns = @JoinColumn(name = "quote_id", referencedColumnName = "quote_id")
	) */
//	private Collection<QuoteEntity> quotes;

	/*
	 * @OneToOne
	 * 
	 * @JoinTable(name = "users_loginattempts", joinColumns = @JoinColumn(name =
	 * "user_id", referencedColumnName = "id"), inverseJoinColumns
	 * = @JoinColumn(name = "login_attempt_id", referencedColumnName = "id"))
	 * private LoginAttemptsEntity loginAttempts;
	 */

	public UserEntity() {
		super("temp", "temp", true, false, false, false, new ArrayList<GrantedAuthority>());
	}

	public UserEntity(String name, String username, String password, String salt, boolean accountLocked, String city,
			String country) {
		super(username, password, true, false, false, !accountLocked, new ArrayList<GrantedAuthority>());
		this.name = name;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.accountLocked = accountLocked;
		this.city = city;
		this.country = country;
	}

	public UserEntity(Long id, String name, String username, String password, String salt, boolean accountLocked,
			String city, String country, RoleEntity role) {
		super(username, password, true, false, false, !accountLocked, new ArrayList<GrantedAuthority>());
		this.name = name;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.accountLocked = accountLocked;
		this.city = city;
		this.country = country;
		this.id = id;
		setRole(role);
	}

	public Long getId() {
		return id;
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

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		// check for attempted change from user role to any other role
		if (this.role != null && this.role.getName() != null && 
		    this.role.getName().equals(Roles.USER.toString()) && 
		    role.equals(Roles.USER.toString()) == false) 
		{
			throw new PrivilegeEscalationException();
		}
				
		this.role = role;
	}

	/*
	public Collection<QuoteEntity> getQuotes() {
		return quotes;
	}

	public void setQuotes(Collection<QuoteEntity> quotes) {
		this.quotes = quotes;
	}
	*/

	public UserResponse convertUserEntityToUserResponse() {
		return new UserResponse(getId(), getName(), getUsername(), getCity(), getCountry());
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username + ", city=" + city + ", country="
				+ country + ", hashedPassword=" + password + ", salt=" + salt + ", accountLocked=" + accountLocked
				+ ", Role: " + role.getName() + "]";
	}
}
