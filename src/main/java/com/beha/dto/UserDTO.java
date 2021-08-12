package com.beha.dto;

import lombok.Data;

@Data
public class UserDTO extends EmailPasswordRequestDTO {
	private String name;
	private String surName;


}
