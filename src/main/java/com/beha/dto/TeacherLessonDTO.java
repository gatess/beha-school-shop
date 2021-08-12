package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class TeacherLessonDTO {

	private Long id;
	private String name;
	//private GradeDTO gradeDTO;
	private List<TeacherShopListDTO> shopLists;
}
