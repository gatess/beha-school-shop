package com.beha.dto;

import java.util.Date;

import com.beha.dao.model.OrderStatus;

import lombok.Data;

@Data
public class OrderHistoryDTO {
	private Long id;
	private Date createdDate;
	private OrderStatusDTO orderStatus;
	private String comment;
	private boolean notice;

}
