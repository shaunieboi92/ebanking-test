package com.ebanking.transactions.kafka;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebanking.common.model.Transaction;
import com.ebanking.transactions.payload.TransactionMsg;

@Service
public class Producer {

	private static final Logger logger = LoggerFactory.getLogger(Producer.class);

	private static final String TOPIC = "test_topic";
	private static final String TXN_TOPIC = "transaction_topic";
	private static final String TXN_MSG_TOPIC = "transaction_msg_topic";
	private static final String TXN_NORMALIZE_TOPIC = "transaction_normalize_topic";
	private static final String TXN_ERROR_TOPIC = "transaction_error_topic";

//	@Autowired
//	@Qualifier("baseTemplate")
//	private KafkaTemplate<String, String> kafkaTemplate;
//	
//	@Autowired
//	@Qualifier("kafkaTxnTemplate")
//	KafkaTemplate<String, Transaction> kafkaTxnTemplate;
//	
//	@Autowired
//	@Qualifier("genericTemplate")
//	KafkaTemplate<String, TransactionMsg> kafkaBaseTemplate;

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	public void sendMessage(String msg) {
		logger.info(String.format("#### -> Producing message -> %s", msg));
		this.kafkaTemplate.send(TOPIC, msg);
	}

	public void sendMessage(Transaction txn) {
		logger.info(String.format("#### -> Producing txn topic: %s -> %s", TXN_TOPIC, txn.getAccountIBAN()));
		this.kafkaTemplate.send(TXN_TOPIC, txn);
	}

	@Transactional
	public void sendMessage(TransactionMsg txn) {
		logger.info(String.format("#### -> Producing txn topic: %s -> %s", TXN_MSG_TOPIC, txn.getAccountIBAN()));
		UUID uuid = UUID.randomUUID();
		logger.info("uuidTransId: " + uuid);
		txn.setUuid(uuid.toString());
//		ProducerRecord<String, TransactionMsg> pRecord = new ProducerRecord<>(TXN_MSG_TOPIC, encodedTxnId, txn);

		this.kafkaTemplate.send(TXN_MSG_TOPIC, uuid.toString(), txn);
	}

	public void normalizeTxnAndMask(TransactionMsg txn) {
		logger.info(String.format("#### -> Producing txn topic: %s -> %s", TXN_NORMALIZE_TOPIC, txn.getAccountIBAN()));
		this.kafkaTemplate.send(TXN_NORMALIZE_TOPIC, txn);
	}

	public void sendTransactionError(TransactionMsg txn) {
		logger.info(String.format("#### -> Producing txn topic: %s -> %s", TXN_ERROR_TOPIC, txn.getAccountIBAN()));
		this.kafkaTemplate.send(TXN_ERROR_TOPIC, txn);
	}
}
