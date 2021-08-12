package com.beha.dto;

import lombok.Data;

@Data
public class OrderHistoryAddDTO {

	private Long orderId;
	private String status;
	private boolean notice;
	private String comment;
}
