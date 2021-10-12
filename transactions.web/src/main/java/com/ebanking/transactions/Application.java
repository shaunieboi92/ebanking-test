package com.ebanking.transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * 
 * @author SHAU0
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableKafka
@EntityScan(basePackages = {"com.ebanking.common.model"})
@EnableJpaRepositories(basePackages={"com.ebanking"})
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}