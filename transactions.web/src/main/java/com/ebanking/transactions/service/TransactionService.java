package com.ebanking.transactions.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ebanking.transactions.payload.TransactionMsg;
import com.ebanking.transactions.payload.TransactionPagedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.querydsl.core.types.Predicate;

public interface TransactionService {

	public TransactionMsg convertCurrencyAndMask(TransactionMsg txnMsg)
			throws JsonMappingException, JsonProcessingException;

	public TransactionPagedResponse getClientTransactions(Predicate predicate, Pageable pageable);

	public TransactionPagedResponse getClientTransactions(Integer pageNo, Integer pageSize, String sortBy,
			String sortDir, String startDate, String endDate, String uuid);

}
