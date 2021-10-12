package com.ebanking.common.model;

import java.math.BigDecimal;
import java.sql.Clob;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "TRANSACTIONS_ERROR_INFO")
public class TransactionErrorInfo extends SecurityAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRANSACTIONS_ERROR_INFO_ID")
	private long transactionId;

	@Column(name = "PRIORITY")
	private String priority;

	@Lob
	@Column(name = "JSON_INFO")
	private Clob jsonInfo;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Clob getJsonInfo() {
		return jsonInfo;
	}

	public void setJsonInfo(Clob jsonInfo) {
		this.jsonInfo = jsonInfo;
	}


}
