package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class LessonTeacherDTO {

	private Long id;
	private String name;
	//private GradeDTO grade;
	private List<ShopListDTO> shopLists;
	private TeacherDTO teacher;
}
