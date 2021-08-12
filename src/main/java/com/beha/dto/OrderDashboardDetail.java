package com.beha.dto;

import lombok.Data;

@Data
public class OrderDashboardDetail {

	private Double comissionTotal;
	private long orderQuantity;
	private Double orderTotal;
	private String date;
}
