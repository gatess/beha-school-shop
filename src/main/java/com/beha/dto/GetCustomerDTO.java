package com.beha.dto;

import lombok.Data;

@Data
public class GetCustomerDTO extends CustomerDTO{
	private String createdDate;
	private long id;
}
