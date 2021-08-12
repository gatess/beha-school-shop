package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDTO {

	private Long userId;
	private long addressId;
	private Double total;
	private String studentName;
	private Long gradeId;
	private List<ShopListDTO> shopList;
	private CreditCardDTO creditCardDTO;
	
}
