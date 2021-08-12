package com.beha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class MessageRequestDTO {
	@JsonProperty("username")
	private String userName;
	private String password;
	@JsonProperty("sdate")
	private String sDate;
	@JsonProperty("vperiod")
	private String vPeriod;
	private Long gate;
	private MessageDTO message;
	
	
}
