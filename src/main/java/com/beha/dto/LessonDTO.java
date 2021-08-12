package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class LessonDTO {

	private Long id;
	private String name;
	private String gradeName;
	private List<ShopListDTO> shopLists;
	private TeacherDTO teacher;
}
