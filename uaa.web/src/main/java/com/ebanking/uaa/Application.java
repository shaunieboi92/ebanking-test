package com.ebanking.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * 
 * @author SHAU0
 *
 */
@SpringBootApplication
@EntityScan(basePackages = {"com.ebanking.common.model"})
@EnableJpaRepositories(basePackages={"com.ebanking"})
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@PropertySource({"classpath:application.properties", "classpath:auth.properties","classpath:gateway.properties"})
@EnableZuulProxy
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}