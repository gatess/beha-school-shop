package com.beha.dto;

import lombok.Data;

@Data
public class UpdateTeacherResponseDTO extends BaseResponseDTO {

	private long teacherId;
	private long lessonId;
	private String teacherName;
	private String lessonName;
}
