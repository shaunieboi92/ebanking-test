package com.ebanking.transactions.util;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.ebanking.common.exception.BadRequestException;
import com.ebanking.common.util.JsonUtils;
import com.ebanking.transactions.kafka.Producer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class TransactionUtil {

	@Value("${conversion.rate.url}")
	private String currencyConvUrl;

	private static final Logger logger = LoggerFactory.getLogger(Producer.class);

	RestTemplate restTemplate = new RestTemplate();

	private final String currencyConvMap = "";

	public Double convertCurrency(String fromCurrency, String toCurrency, Double amount)
			throws JsonMappingException, JsonProcessingException, BadRequestException {
		Double rslt = null;
		Double conversionRate = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		try {
			String resp = restTemplate.getForObject(currencyConvUrl + toCurrency, String.class);
			Map<String, Object> respMap = JsonUtils.toJson(resp);
			@SuppressWarnings("unchecked")
			Map<String, Object> conversionRateMap = (Map<String, Object>) respMap.get("conversion_rates");
			logger.info(respMap.get("conversion_rates").toString());
			if (ObjectUtils.isEmpty(conversionRateMap.get(fromCurrency))) {
				logger.error("Error in currency");
				throw new BadRequestException("Error in currency");
			} else
				conversionRate = (Double) conversionRateMap.get(fromCurrency);
			rslt = amount / conversionRate;
//			rslt = amount / 1.37;
		} catch (HttpServerErrorException e) {
			logger.debug("HttpServer Exception:" + e.getMessage());
		}
		return rslt;
	}

	public String maskAccIBAN(String accountIBAN) {
		String masked = StringUtils.overlay(accountIBAN, StringUtils.repeat("X", accountIBAN.length() - 4), 0,
				accountIBAN.length() - 4);
		logger.info("masked IBAN: " + masked);
		return masked;
	}
}
