package com.ebanking.uaa.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String token;

	public JwtResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

}