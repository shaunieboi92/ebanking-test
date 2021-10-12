package com.ebanking.transactions.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.ebanking.common.model.Transaction;
import com.ebanking.transactions.payload.TransactionMsg;
import com.ebanking.transactions.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class Consumer {
	
	//prevent poison pill for kafka consumer

	@Autowired
	TransactionService txnService;

	private static final Logger logger = LoggerFactory
			.getLogger(Consumer.class);

	@KafkaListener(topics = "test_topic", groupId = "group_id")
	public void consumeMsg(String msg) {
		logger.info(msg);
	}

	@KafkaListener(topics = "transaction_topic", groupId = "group_json_id", containerFactory = "kafkaListenerContainerFactoryTxn")
	public void consumeTxnMsg(Transaction txn) {
		logger.info("consumed json msg:" + txn);

	}

	/**
	 * Receives raw money transactions and normalizes to USD and masks acc IBAN before storage
	 * @param txn
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@KafkaListener(topics = "transaction_msg_topic", groupId = "group_txn_msg_id", containerFactory = "kafkaListenerContainerFactoryTxnMsg")
	public void consumeTxnMsg(TransactionMsg txn)
			throws JsonMappingException, JsonProcessingException {

//	public void consumeTxnMsg(ConsumerRecord<String,TransactionMsg> cr, @Payload TransactionMsg payload)
//			throws JsonMappingException, JsonProcessingException {
//		logger.info("Received key: {} | consumed json msg: {} ", cr.key(), payload);
		// produce to another topic
		// Converting to USD
		//masks credit card no
		txnService.convertCurrencyAndMask(txn);
		
	}

}
