package com.beha.dto;

import lombok.Data;

@Data
public class MailOrderHistoryDTO extends MailDTO {

	private String comment;
	private String orderStatus;
	
}
