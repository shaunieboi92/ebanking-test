package com.ebanking.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	public static Map<String,Object> toJson(String jsonString)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> prop = new ConcurrentHashMap<>();
		prop = mapper.readValue(jsonString,
				new TypeReference<Map<String, Object>>() {
				});
		return prop;
	}
}
