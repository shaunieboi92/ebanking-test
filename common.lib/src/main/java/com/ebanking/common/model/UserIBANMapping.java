package com.ebanking.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USER_IBAN_MAPPING")
public class UserIBANMapping extends SecurityAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_IBAN_MAPPING_ID")
	private long userIBANMappingId;

	@Column(name = "IBAN_NO")
	private String IBAN;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "USER_ID")
	private User user;

	public long getUserIBANMappingId() {
		return userIBANMappingId;
	}

	public void setUserIBANMappingId(long userIBANMappingId) {
		this.userIBANMappingId = userIBANMappingId;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserIBANMapping(String iBAN, User user, String createdBy) {
		super();
		IBAN = iBAN;
		this.user = user;
		this.setCreatedBy(createdBy);
	}

	public UserIBANMapping() {
		super();
	}

}
