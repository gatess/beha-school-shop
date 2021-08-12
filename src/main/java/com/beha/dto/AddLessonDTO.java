package com.beha.dto;

import lombok.Data;

@Data
public class AddLessonDTO {
	
	long schoolId;
	String lessonName;
	String gradeName;
	long teacherId;

}
