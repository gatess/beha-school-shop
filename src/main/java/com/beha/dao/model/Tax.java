package com.beha.dao.model;

import java.math.BigDecimal;

public enum Tax {

	KDV_0(0 , BigDecimal.valueOf(0)),
    KDV_1(1, BigDecimal.valueOf(1)),
    KDV_8(2, BigDecimal.valueOf(8)),
    KDV_18(3, BigDecimal.valueOf(18));

	private int value;
	private BigDecimal taxRate;
	
	private Tax(int value, BigDecimal taxRate) {
		this.value = value;
		this.taxRate = taxRate;
		
	}
	
	public int getValue() {
		return value;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}
}
