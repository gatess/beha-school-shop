package com.beha.dto;

import lombok.Data;

@Data
public class TeacherShopListDTO {
	//private Long id;
		private Integer quantity;
		private Double price;
		private TeacherProductDTO productDto;
}
