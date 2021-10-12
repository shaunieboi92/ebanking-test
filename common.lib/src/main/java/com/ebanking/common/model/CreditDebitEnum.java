package com.ebanking.common.model;

import java.util.HashMap;
import java.util.Map;

public enum CreditDebitEnum {
	CREDIT(0, "credit"), DEBIT(1, "debit");

	private Integer creditCode;

	private String desc;

	// Reverse-lookup map for getting a day from an abbreviation
	private static final Map<Integer, CreditDebitEnum> lookup = new HashMap<Integer, CreditDebitEnum>();

	static {
		for (CreditDebitEnum d : CreditDebitEnum.values()) {
			lookup.put(d.getCreditCode(), d);
		}
	}

	static public CreditDebitEnum fromCode(Integer code) {
		CreditDebitEnum creditDebit = null;
		if (code != null) {
			creditDebit = lookup.get(code);
		}
		return creditDebit;
	}

	private CreditDebitEnum(int creditCode, String desc) {
		this.creditCode = creditCode;
		this.desc = desc;
	}

	public int getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(int creditCode) {
		this.creditCode = creditCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
