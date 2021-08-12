package com.beha.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.Order;
import com.beha.dao.model.OrderDetail;

public interface OrderDetailRepository  extends JpaRepository<OrderDetail, Long>, JpaSpecificationExecutor<OrderDetail> {

	List<OrderDetail> findByOrder(Order order);
	List<OrderDetail> findByOrderId(Long orderId);
}
