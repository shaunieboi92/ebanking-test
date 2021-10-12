package com.ebanking.transactions.payload;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String uuid;

	private Double amt;

	private String currency;

	private String accountIBAN;

	private LocalDateTime transactionDate = LocalDateTime.now();

	private String description;
	
	private Integer creditDebitType;

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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
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

	public Integer getCreditDebitType() {
		return creditDebitType;
	}

	public void setCreditDebitType(Integer creditDebitType) {
		this.creditDebitType = creditDebitType;
	}

	/**
     * Kafka Serializer implementation.
     * Serializes a Transaction to JSON so it can be sent to a Kafka Topic
     */
    public static class TransactionMsgSerializer implements Serializer<TransactionMsg> {
        @Override
        public byte[] serialize(String topic, TransactionMsg data) {
            byte[] serializedData = null;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                serializedData = objectMapper.writeValueAsString(data).getBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return serializedData;
        }
    }

	@Override
	public String toString() {
		return "TransactionMsg [uuid="
				+ uuid + ", amt=" + amt + ", currency=" + currency
				+ ", accountIBAN=" + accountIBAN + ", transactionDate="
				+ transactionDate + ", description=" + description
				+ ", creditDebitType=" + creditDebitType + "]";
	}



}
