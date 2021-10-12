package com.ebanking.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PERMISSIONS")
public class Permissions extends SecurityAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PERMISSIONS_ID")
	private long permissionsId;

	@Column(name = "PERMISSION")
	private String permission;

	@ManyToMany(mappedBy = "permission")
	private Set<User> users;

	public Permissions(String permission, Set<User> users, String createdBy) {
		super();
		this.permission = permission;
		this.users = users;
		this.setCreatedBy(createdBy);
	}

	public long getPermissionsId() {
		return permissionsId;
	}

	public void setPermissionsId(long permissionsId) {
		this.permissionsId = permissionsId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Permissions() {
		super();
	}

}
