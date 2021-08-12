package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class TeacherProductDTO {

	private String barcode;
	private String name;
	private List<ProductImageDTO> productImages;
}
