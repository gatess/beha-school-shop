package com.beha.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

	private String message;
	private String status;
	private UserInformationDTO userInformationDTO;
}
