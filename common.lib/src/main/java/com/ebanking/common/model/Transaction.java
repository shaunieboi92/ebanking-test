package com.ebanking.common.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "ACC_TRANSACTIONS")
public class Transaction extends SecurityAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ACC_TXN_ID")
	private long transactionId;

	@Column(name = "UUID", unique = true)
	private String uuid;

	@Column(name = "AMOUNT")
	private Double amt;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CURRENCY_ID")
	private Currency currency;

	@Column(name = "IBAN")
	private String accountIBAN;

	@Column(name = "TXN_DATE")
	private LocalDateTime transactionDate;

	@Column(name = "DESCRIPTION")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "CREDITDEBIT_TYPE")
	private CreditDebitEnum creditDebitType;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getAccountIBAN() {
		return accountIBAN;
	}

	public void setAccountIBAN(String accountIBAN) {
		this.accountIBAN = accountIBAN;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CreditDebitEnum getCreditDebitType() {
		return creditDebitType;
	}

	public void setCreditDebitType(CreditDebitEnum creditDebitType) {
		this.creditDebitType = creditDebitType;
	}

//	/**
//     * Kafka Serializer implementation.
//     * Serializes a Transaction to JSON so it can be sent to a Kafka Topic
//     */
//    public static class TransactionSerializer implements Serializer<Transaction> {
//        @Override
//        public byte[] serialize(String topic, Transaction data) {
//            byte[] serializedData = null;
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                serializedData = objectMapper.writeValueAsString(data).getBytes();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return serializedData;
//        }
//    }

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", uuid=" + uuid + ", amt=" + amt + ", currency="
				+ currency + ", accountIBAN=" + accountIBAN + ", transactionDate=" + transactionDate + ", description="
				+ description + ", creditDebitType=" + creditDebitType + "]";
	}

}
