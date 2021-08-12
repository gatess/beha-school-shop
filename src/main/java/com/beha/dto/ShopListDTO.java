package com.beha.dto;

import lombok.Data;

@Data
public class ShopListDTO {
	private Long id;
	private Integer quantity;
	private Double price;
	private ProductDTO product;

}
