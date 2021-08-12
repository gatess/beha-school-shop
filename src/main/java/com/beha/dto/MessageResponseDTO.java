package com.beha.dto;

import lombok.Data;

@Data
public class MessageResponseDTO {

	private String status;
	private String error;
	private MessageResponseResultDTO result;
}
