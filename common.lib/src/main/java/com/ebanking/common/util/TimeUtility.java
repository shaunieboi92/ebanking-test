package com.ebanking.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class TimeUtility {

	static final String datePattern = "yyyy-MM-dd";

	public static LocalDate convertStringToLocalDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
		LocalDate localDate = LocalDate.parse(dateString, formatter);
		return localDate;
	}
	
	public static LocalDateTime convertStringToLocalDateTime(String dateString, LocalTime localTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
		LocalDateTime localDatetime = LocalDate.parse(dateString, formatter).atTime(localTime);
		return localDatetime;
	}
	
//	public static String convertLocalDateTimeToString( LocalDateTime ldt) {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
//		LocalDateTime localDatetime = LocalDate.parse(dateString, formatter).atTime(localTime);
//		return localDatetime;
//	}

}
