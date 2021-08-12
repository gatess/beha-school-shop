package com.beha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class MessageResponseResultDTO {

	@JsonProperty("messageid")
	private String messageId;
	private String count;
	private String kredi;
}
