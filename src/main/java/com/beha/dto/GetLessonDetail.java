package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class GetLessonDetail {
	private String name;
	private List<GetProductDTO> product;

}
