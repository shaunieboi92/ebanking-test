package com.ebanking.uaa.payload;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;


	private String uuid;

	@JsonProperty(value = "full_name")
	private String fullName;

	private Short deleted;
	
	private Set<String>permissions;
	
	private Set<String>accountIban;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Short getDeleted() {
		return deleted;
	}

	public void setDeleted(Short deleted) {
		this.deleted = deleted;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public Set<String> getAccountIban() {
		return accountIban;
	}

	public void setAccountIban(Set<String> accountIban) {
		this.accountIban = accountIban;
	}

}
