package com.ebanking.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USER")
public class User extends SecurityAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private long userId;

	@Column(name = "USER_FULL_NAME")
	private String userFullName;

	@Column(name = "LOGIN_ID")
	private String loginId;

	@Column(name = "SALTED_PASSWORD")
	private String password;

	@Column(name = "UUID")
	private String uuid;

	@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.MERGE
            })
	private Set<Permissions> permission;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	@JsonIgnore
	private List<UserIBANMapping> ibanList = new ArrayList<>();

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public List<UserIBANMapping> getIbanList() {
		return ibanList;
	}

	public void setIbanList(List<UserIBANMapping> ibanList) {
		this.ibanList = ibanList;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Permissions> getPermission() {
		return permission;
	}

	public void setPermission(Set<Permissions> permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userFullName=" + userFullName
				+ ", loginId=" + loginId + ", password=" + password + ", uuid="
				+ uuid + ", permission=" + permission + ", ibanList=" + ibanList
				+ "]";
	}



}
