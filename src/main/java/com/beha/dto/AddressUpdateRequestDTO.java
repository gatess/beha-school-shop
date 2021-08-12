package com.beha.dto;

import lombok.Data;

@Data
public class AddressUpdateRequestDTO {

	private String addressName;
	private String addressDescription;
	private String province;
	private String county;
	private String zipCode;
	private String telephone;
	private Long userId;
	private Long id;
}
