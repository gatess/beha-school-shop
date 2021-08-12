package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class GradeDTO {

	private Long id;
	private String name;
	private List<LessonDTO> lessons;
}
