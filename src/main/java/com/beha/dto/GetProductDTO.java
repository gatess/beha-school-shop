package com.beha.dto;

import lombok.Data;

@Data
public class GetProductDTO {

	private String barcode;
	private String name;
	private Double price;
	private Integer quantity;
	private String image;
}
