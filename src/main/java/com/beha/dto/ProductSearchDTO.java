package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductSearchDTO {

	List<ProductDTO> productList;
	Boolean success;
	String message;
}
