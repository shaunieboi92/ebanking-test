package com.ebanking.transactions.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebanking.common.model.Transaction;
import com.ebanking.transactions.kafka.Producer;
import com.ebanking.transactions.payload.TransactionMsg;
import com.ebanking.transactions.payload.TransactionPagedResponse;
import com.ebanking.transactions.service.TransactionService;
import com.ebanking.transactions.util.TransactionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.querydsl.core.types.Predicate;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionsController {

	@Autowired
	private Producer producer;

	@Autowired
	private TransactionService txnService;

//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "get a listing of money account transactions by date")})
//	@GetMapping("/get_customer_transactions")
//	public TransactionPagedResponse getClientTransactions(
//			@QuerydslPredicate(root = Transaction.class) Predicate predicate,
//			Pageable pageable) {
//		return txnService.getClientTransactions(predicate, pageable);
//
//	}
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "get a listing of money account transactions by date")})
	@PreAuthorize("hasRole('CLIENT')")
	@GetMapping("/get_customer_transactions")
	public TransactionPagedResponse getClientTransactions(
			 @RequestParam(defaultValue = "0") Integer pageNo, 
             @RequestParam(defaultValue = "10") Integer pageSize,
             @RequestParam(defaultValue = "transactionDate") String sortBy,
             @RequestParam(defaultValue = "asc")String sortDir,
             @RequestParam String startDate, @RequestParam String endDate,
             @RequestHeader String uuid) {
		return txnService.getClientTransactions(pageNo, pageSize, sortBy, sortDir, startDate, endDate, uuid);

	}
	

	@PostMapping("/publish")
	public void messageToTopic(@RequestParam(name = "message") String msg) {
		this.producer.sendMessage(msg);
	}

	@PostMapping("/publish_txn")
	public void messageToTxnTopic(@RequestBody Transaction txn) {
		this.producer.sendMessage(txn);
	}

	@PostMapping("/publish_txnMsg")
	public void messageToTxnMsgTopic(@RequestBody TransactionMsg txn) {
		this.producer.sendMessage(txn);
	}

	// @PostMapping("/get_txn")
	// public void messageToTxnTopic() {
	// this.producer.sendMessage(txn);
	// }

	// }

}
