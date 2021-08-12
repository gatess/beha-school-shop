package com.beha.dto;

import lombok.Data;

@Data
public class TeacherDTO extends EmailPasswordRequestDTO {

	private String name;
	private long id;

}
