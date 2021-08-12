package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class SchoolAdminDashboardDTO extends AdminDashboardDTO {

	private String schoolName;
	private long schoolId;
	private List<GradeLessonDTO> gradeLessonDto;
	private List<TeacherDTO> teacherDto;
}
