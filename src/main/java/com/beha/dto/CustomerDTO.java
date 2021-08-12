package com.beha.dto;

import java.util.List;

import lombok.Data;
@Data
public class CustomerDTO {

	private String name;
	private String surname;
	private String email;
	private List<AddressDTO> address;

}
