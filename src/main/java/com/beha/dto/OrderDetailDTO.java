package com.beha.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
	private ProductDTO product;
	private Integer quantity;
	private Double price;

}
