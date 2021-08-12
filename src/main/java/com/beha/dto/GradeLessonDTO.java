package com.beha.dto;

import lombok.Data;

@Data
public class GradeLessonDTO {

	private String gradeName;
	private String lessonName;
	private long gradeId;
	private long lessonId;
	private String teacherName;
	private long teacherId;
}
