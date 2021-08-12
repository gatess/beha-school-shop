package com.beha.dto;

import lombok.Data;

@Data
public class AddTeacherDTO extends EmailPasswordRequestDTO {

	private String name;
	private long schoolId;
}
