package com.beha.dto;

import java.util.List;

import com.beha.dao.model.OrderStatus;

import lombok.Data;
import java.util.Date;
@Data
public class OrderResponseDTO {

	private Double total;
	private String studentName;
	private Long gradeId;
	private OrderStatus orderStatus;
	private String orderStatusDescription;
	private Long orderNumber;
	private String createdDate;
	private String customer;
	private String gradeName;
	private String schoolName;
	private List<OrderDetailDTO> orderDetailList;
	private AddressDTO addressDTO;
	private List<OrderHistoryDTO> orderHistoryList;
	
	
}
