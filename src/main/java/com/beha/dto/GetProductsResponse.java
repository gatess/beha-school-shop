package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class GetProductsResponse {

	private String message;
	private String status;
	private List<GetProductDTO> product;
	
}
