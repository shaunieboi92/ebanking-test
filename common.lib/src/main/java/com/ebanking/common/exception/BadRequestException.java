package com.ebanking.common.exception;

public class BadRequestException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String code;

	public BadRequestException(String message) {
		super(message);
		this.code = "parameter_missing";
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
		this.code = "parameter_missing";
	}

	public BadRequestException(String message, String code) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
