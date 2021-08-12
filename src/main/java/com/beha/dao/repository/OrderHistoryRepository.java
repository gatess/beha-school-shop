package com.beha.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.OrderHistory;

public interface OrderHistoryRepository  extends JpaRepository<OrderHistory, Long>, JpaSpecificationExecutor<OrderHistory> {

}
