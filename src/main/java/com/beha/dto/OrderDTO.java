package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {

	private Double total;
	private String studentName;
	private CustomerDTO customer;
	private Long gradeId;
	private Double commissionAmount;
	private List<ShopListDTO> shopList;
	private CreditCardDTO creditCardDTO;
}
