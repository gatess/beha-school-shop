package com.beha.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beha.dao.model.Order;
import com.beha.dao.model.OrderStatus;

public interface OrderRepository  extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

	@Query("select o from Order o where customer_id = :customerId and order_status_id!=9")
	List<Order> findByCustomerIdAndOrderStatus(@Param("customerId") Long customerId);
	List<Order> findByOrderStatus(OrderStatus orderStatus);
	
	@Query("select count (*) from Order o where order_status_id=1")
	long getNewOrdersCount();
	
	@Query("select count (*) from Order o where order_status_id!=9 and order_status_id!=5")
	long getAllOrdersCount();
	
	@Query("From Order where orderStatus!='STATUS_PAYMENT_ERROR' and orderStatus!='STATUS_INVALID' order by createdDate desc")
	List<Order> getAllOrders();
	
	@Query("select SUM(o.total) from Order o where order_status_id!=0")
	Double getCompletedOrdersSum();
	
	@Query("select SUM(o.commissionAmount) from Order o where order_status_id!=0")
	Double getCompletedOrdersComissionAmount();
	
	@Query("select  count (*) from Order o where date_added > current_date -25")
	long getWeeklyOrdersCount();
	
	@Query("FROM Order WHERE date_added BETWEEN :date1 AND :date2")
	List<Order> getOrderBetweenDates(@Param("date1")Date date1,@Param("date2")Date date2);
	
	@Query("FROM Order WHERE date_added LIKE :date%")
	List<Order> getDailyOrders(@Param("date")String date);
	
	@Query("select o from Order o where date_added > current_date -7")
	List<Order> getLastWeekOrders();
}
