package com.ebanking.transactions.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ebanking.common.exception.BadRequestException;
import com.ebanking.common.model.CreditDebitEnum;
import com.ebanking.common.model.QTransaction;
import com.ebanking.common.model.Transaction;
import com.ebanking.common.model.User;
import com.ebanking.common.model.UserIBANMapping;
import com.ebanking.common.payload.PagedResponse;
import com.ebanking.common.repository.CurrencyRepository;
import com.ebanking.common.repository.TransactionRepository;
import com.ebanking.common.repository.UserRepository;
import com.ebanking.common.util.Constants;
import com.ebanking.common.util.PageUtil;
import com.ebanking.common.util.TimeUtility;
import com.ebanking.transactions.kafka.Producer;
import com.ebanking.transactions.payload.TransactionMsg;
import com.ebanking.transactions.payload.TransactionPagedResponse;
import com.ebanking.transactions.service.TransactionService;
import com.ebanking.transactions.util.TransactionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionUtil txnUtil;

	@Autowired
	TransactionRepository txnRepository;

	@Autowired
	CurrencyRepository currencyRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	Producer producer;

	private static final Logger logger = LoggerFactory
			.getLogger(TransactionServiceImpl.class);

	@Transactional
	@Override
	public TransactionMsg convertCurrencyAndMask(TransactionMsg txnMsg)
			throws JsonMappingException, JsonProcessingException {
		try {
			Double amtUSD = txnUtil.convertCurrency(txnMsg.getCurrency(), "USD",
					txnMsg.getAmt());
			txnMsg.setAmt(amtUSD);
			txnMsg.setCurrency("USD");
			String maskedIBAN = txnUtil.maskAccIBAN(txnMsg.getAccountIBAN());
			logger.info("new txn msg->" + txnMsg.toString());
			Transaction txn = mapTransaction(txnMsg);
			logger.info("saving txn");
			txnRepository.save(txn);
			txnMsg.setAccountIBAN(maskedIBAN);

			producer.normalizeTxnAndMask(txnMsg);

		} catch (BadRequestException e) {
			logger.debug("routing error txn");
			producer.sendTransactionError(txnMsg);
		}
		return txnMsg;

	}

	private Transaction mapTransaction(TransactionMsg txnMsg) {
		Transaction txn = new Transaction();
		txn.setAccountIBAN(txnMsg.getAccountIBAN());
		txn.setAmt(txnMsg.getAmt());
		txn.setCurrency(
				currencyRepository.findByCurrencyCode(txnMsg.getCurrency()));
		txn.setDescription(txnMsg.getDescription());
		txn.setTransactionDate(txnMsg.getTransactionDate());
		txn.setAccountIBAN(txnMsg.getAccountIBAN());
		CreditDebitEnum creditDebitEnum = CreditDebitEnum
				.fromCode(txnMsg.getCreditDebitType());
		logger.info("credit type: " + creditDebitEnum);
		txn.setCreditDebitType(creditDebitEnum);
		txn.setUuid(txnMsg.getUuid());
		txn.setCreatedBy(Constants.SYSTEM);
		return txn;
	}

	@Override
	public TransactionPagedResponse getClientTransactions(Predicate predicate,
			Pageable pageable) {
		BooleanBuilder where = new BooleanBuilder();
		where.and(predicate)
				.and(QTransaction.transaction.deleted.eq(Constants.FALSE));
		// Page<Transaction> transactionPage = txnRepository.findAll(where,
		// pageable);
		// List<TransactionMsg> transactionList = transactionPage.stream().map(x
		// -> mapToTransactionResp(x))
		// .collect(Collectors.toList());
		// TransactionPagedResponse resp = new TransactionPagedResponse();
		// resp.setTransactionResponseList(transactionList);
		// return resp;
		return null;

	}

	@Override
	public TransactionPagedResponse getClientTransactions(Integer pageNo,
			Integer pageSize, String sortBy, String sortDir, String startDate,
			String endDate, String uuid) {
		Pageable paging = PageRequest.of(pageNo, pageSize,
				sortDir.equalsIgnoreCase("asc")
						? Sort.by(sortBy).ascending()
						: Sort.by(sortBy).descending());
		logger.info("uuid is now:" + uuid);
		LocalDateTime startDateConv = TimeUtility
				.convertStringToLocalDateTime(startDate, LocalTime.MIN);
		LocalDateTime endDateConv = TimeUtility
				.convertStringToLocalDateTime(endDate, LocalTime.MAX);
		// List<UserIBANMapping> accountIBANList = Optional
		// .ofNullable(userRepository.findIbanListByUuid(uuid))
		// .orElseThrow(() -> new BadRequestException("iban not found"));
		User user = Optional.ofNullable(userRepository.findByUuid(uuid))
				.orElseThrow(() -> new BadRequestException("User not found"));
		List<String> ibanList = null;
		if (CollectionUtils.isEmpty(user.getIbanList())) {
			logger.info("iban is empty");
		} else {
			ibanList = user.getIbanList().stream().map(x -> {
				logger.info("iban is :" + x);
				return x.getIBAN();
			}).collect(Collectors.toList());
		}
		TransactionPagedResponse resp = null;
		Page<Transaction> pagedResult = txnRepository
				.findByTransactionDateBetweenAndAccountIBANIn(startDateConv,
						endDateConv, ibanList, paging);
		if (pagedResult.hasContent()) {
			List<TransactionMsg> transactionList = pagedResult.stream()
					.map(x -> mapToTransactionResp(x))
					.collect(Collectors.toList());
			resp = new TransactionPagedResponse();
			resp.setTransactionResponseList(transactionList);
			PageUtil.mapPageData(resp, pagedResult);
		}
		return resp;
	}

	private TransactionMsg mapToTransactionResp(Transaction txn) {
		TransactionMsg txnMsg = new TransactionMsg();
		txnMsg.setAccountIBAN(txnUtil.maskAccIBAN(txn.getAccountIBAN()));
		txnMsg.setAmt(txn.getAmt());
		txnMsg.setCreditDebitType(txn.getCreditDebitType().getCreditCode());
		txnMsg.setCurrency(txn.getCurrency().getCurrencyCode());
		txnMsg.setDescription(txn.getDescription());
		txnMsg.setUuid(txn.getUuid());
		txnMsg.setTransactionDate(txn.getCreatedOn());

		return txnMsg;
	}
	

}
