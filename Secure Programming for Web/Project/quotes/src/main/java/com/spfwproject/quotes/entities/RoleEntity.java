package com.spfwproject.quotes.entities;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.spfwproject.quotes.constants.Roles;
import com.spfwproject.quotes.exceptions.PrivilegeEscalationException;

@Entity
@Table(name = "Roles")
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="role_id")
	private Long id;

	@NotEmpty
	private String name;

	// @ManyToMany(mappedBy = "roles")
//	private Collection<UserEntity> users;
 
	// previous setup with a foreign key placed in the other table
	// @OneToOne
  //@JoinColumn(name = "user_id")
	
	
	/*
	 * @OneToOne
	 * 
	 * @JoinTable( name = "User_Role", joinColumns = @JoinColumn(name = "B_ID"),
	 * inverseJoinColumns = @JoinColumn(name = "A_ID") )
	 */
   // private UserEntity user;

//	@ManyToMany
//	@JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
//	private Collection<PrivilegeEntity> privileges;

	public RoleEntity() {
	}

	public RoleEntity(String role) {
		setName(role);
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

	public void setName(String newRoleName) {
		
		// check for attempted change from user role to any other role
		if (this.name != null && 
			this.name.equals(Roles.USER.toString()) && 
			newRoleName.equals(Roles.USER.toString()) == false) 
		{
			throw new PrivilegeEscalationException();
		}
		
		Roles role = Roles.valueOf(name);
		this.name = role.toString();
	}
	/*
	 * public UserEntity getUser() { return user; }
	 * 
	 * public void setUser(UserEntity user) { this.user = user; }
	 */
	/*

	public Collection<PrivilegeEntity> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Collection<PrivilegeEntity> privileges) {
		this.privileges = privileges;
	}
	*/

}
