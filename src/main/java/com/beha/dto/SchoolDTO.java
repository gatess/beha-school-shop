package com.beha.dto;

import lombok.Data;

@Data
public class SchoolDTO {
	private Long id;
	private String name;
	private String subdomain;
	private String campus;
	private String city;
	private String town;
	private Double commissionRate;

}
