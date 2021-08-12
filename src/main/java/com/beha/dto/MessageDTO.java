package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class MessageDTO {

	private String sender;
	private String text;
	private String utf8;
	private List<String> gsm;
}
