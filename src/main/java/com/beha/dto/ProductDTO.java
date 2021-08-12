package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductDTO {

	private String barcode;
	private String name;
	private List<ProductImageDTO> productImages;
	//private Double price;

}
