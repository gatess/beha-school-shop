package com.beha.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beha.dao.model.Order;
import com.beha.dao.model.School;
import com.beha.dao.model.SchoolUser;

public interface SchoolUserRepository  extends JpaRepository<SchoolUser, Long>, JpaSpecificationExecutor<SchoolUser>{

	SchoolUser findByEmail(String email);
	
	@Query("select count (*) from Order o where order_status_id=1 and o.grade.school.id=:school_id")
	long getNewOrdersCount(@Param("school_id")long school_id);
	
	@Query("select count (*) from Order o where order_status_id!=9 and order_status_id!=5 and o.grade.school.id=:school_id")
	long getAllOrdersCount(@Param("school_id")long school_id);
	
	@Query("select SUM(o.total) from Order o where order_status_id!=0  and o.grade.school.id=:school_id")
	Double getCompletedOrdersSum(@Param("school_id")long school_id);
	
	@Query("select SUM(o.commissionAmount) from Order o where order_status_id!=0 and o.grade.school.id=:school_id")
	Double getCompletedOrdersComissionAmount(@Param("school_id")long school_id);
	
	@Query("select o from Order o where date_added > current_date -7 and o.grade.school.id=:school_id")
	List<Order> getLastWeekOrders(@Param("school_id")long school_id);
	
	}
