package com.ebanking.transactions.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.ebanking.common.model.Transaction;
import com.ebanking.transactions.payload.TransactionMsg;

@Configuration
public class KafkaConfig<T extends Object> {

	private KafkaTemplate<String, Object> template;

	public ProducerFactory<String, Object> txnBaseProducerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "-1");
		return new DefaultKafkaProducerFactory<>(config);

	}

//	public ProducerFactory<String, Transaction> txnProducerFactory() {
//		Map<String, Object> config = new HashMap<>();
//		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//				StringSerializer.class);
//		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//				JsonSerializer.class);
//		return new DefaultKafkaProducerFactory<>(config);
//
//	}

	public ProducerFactory<String, String> stringProducerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "-1");
		return new DefaultKafkaProducerFactory<>(config);

	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<String, Object>(txnBaseProducerFactory());
	}

//	@Bean(name = "kafkaTxnTemplate")
//	KafkaTemplate<String, Transaction> kafkaTxnTemplate() {		
//		return new KafkaTemplate<String, Transaction>(txnBaseProducerFactory());
//	}

//	@Bean(name = "genericTemplate")
//	KafkaTemplate<String, TransactionMsg> kafkaBaseTemplate() {
//		return new KafkaTemplate<String, TransactionMsg>(txnBaseProducerFactory());
//	}
//
//	@Bean(name = "baseTemplate")
//	KafkaTemplate<String, String> kafkaTemplate() {
//		return new KafkaTemplate<String, String>(stringProducerFactory());
//	}

	@Bean
	public ConsumerFactory<String, Transaction> consumerFactoryTxn() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_json_id");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
				new JsonDeserializer<>(Transaction.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Transaction> kafkaListenerContainerFactoryTxn() {
		ConcurrentKafkaListenerContainerFactory<String, Transaction> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryTxn());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, TransactionMsg> consumerFactoryTxnMsg() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_txn_msg_id");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.ebanking.transactions.payload");
		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
				new JsonDeserializer<>(TransactionMsg.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, TransactionMsg> kafkaListenerContainerFactoryTxnMsg() {
		ConcurrentKafkaListenerContainerFactory<String, TransactionMsg> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryTxnMsg());
		return factory;
	}
}
