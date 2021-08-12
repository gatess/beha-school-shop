package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class GetLesson {
	private String message;
	private String status;
	private List<GetLessonDetail> getLessonDetail;

}
