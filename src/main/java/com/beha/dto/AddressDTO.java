package com.beha.dto;

import lombok.Data;

@Data
public class AddressDTO {

	private Long id;
	private String addressName;
	private String addressDescription;
	private String county;
	private String province;
	private String zipCode;
	private String telephone;
}
