package com.beha.dto;

import lombok.Data;

@Data
public class CreditCardDTO {

	private String cardHolderName;
	private String cardNumber;
	private String cardExpireMonth;
	private String cardExpireYear;
	private String cvc;
}
