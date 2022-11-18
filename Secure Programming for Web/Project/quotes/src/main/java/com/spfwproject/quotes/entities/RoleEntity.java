package com.spfwproject.quotes.entities;

import java.util.Collection;
import javax.persistence.*;

import com.spfwproject.quotes.constants.Roles;


@Entity
@Table(name = "Roles")
public class RoleEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;
    
    @ManyToMany
    @JoinTable(
        name = "roles_privileges", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "id"))
    private Collection<PrivilegeEntity> privileges;
    
    public RoleEntity() {}
    
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

	public void setName(String name) {
		Roles role = Roles.valueOf(name); 
		this.name = role.toString();
	}

	public Collection<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Collection<UserEntity> users) {
		this.users = users;
	}

	public Collection<PrivilegeEntity> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Collection<PrivilegeEntity> privileges) {
		this.privileges = privileges;
	}
	
}
