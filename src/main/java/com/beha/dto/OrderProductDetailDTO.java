package com.beha.dto;

import lombok.Data;

@Data
public class OrderProductDetailDTO {

	private Integer quantity;
	private Double price;
	private String barcode;
	private String name;
}
