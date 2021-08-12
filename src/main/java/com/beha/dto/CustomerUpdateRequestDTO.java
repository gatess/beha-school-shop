package com.beha.dto;

import lombok.Data;

@Data
public class CustomerUpdateRequestDTO {

	private Long userId;
	private String name;
	private String surname;
	private String email;
}
