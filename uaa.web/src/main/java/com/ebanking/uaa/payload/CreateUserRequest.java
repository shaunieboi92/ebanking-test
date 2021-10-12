package com.ebanking.uaa.payload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@JsonProperty(value = "first_name")
	private String firstName;

	@NotNull
	@JsonProperty(value = "last_name")
	private String lastName;

	@NotNull
	@JsonProperty(value = "username")
	private String username;

	@NotNull
	private List<String> ibanList = new ArrayList<>();

	@NotNull
	@JsonProperty(value = "pwd")
	private String pwd;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public List<String> getIbanList() {
		return ibanList;
	}

	public void setIbanList(List<String> ibanList) {
		this.ibanList = ibanList;
	}

}
