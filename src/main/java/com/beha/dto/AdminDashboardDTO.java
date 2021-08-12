package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class AdminDashboardDTO {
	private String adminName;
	private long adminId;
	private String errorMessage;
	private String status;
	private long newOrderQuantity;
	private long allOrderQuantity;
	private long allCustomerQuantity;
	private long allProductQuantity;
	private Double ordersSum;
	private Double comissionSum;
	private List<OrderDashboardDetail> orderDashboardDetail;
	List<Integer> customerQuantity;
}
