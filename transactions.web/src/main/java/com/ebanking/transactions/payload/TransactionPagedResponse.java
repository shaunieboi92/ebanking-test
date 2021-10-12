package com.ebanking.transactions.payload;

import java.util.List;

import com.ebanking.common.payload.PagedResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionPagedResponse extends PagedResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("transactions")
	private List<TransactionMsg> transactionResponseList;

	public List<TransactionMsg> getTransactionResponseList() {
		return transactionResponseList;
	}

	public void setTransactionResponseList(List<TransactionMsg> transactionResponseList) {
		this.transactionResponseList = transactionResponseList;
	}
	
	

}
