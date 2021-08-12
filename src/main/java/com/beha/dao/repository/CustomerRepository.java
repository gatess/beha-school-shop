package com.beha.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beha.dao.model.Customer;
import com.beha.dao.model.Order;


public interface CustomerRepository  extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer>{
	boolean existsByEmail(String email);
	
	Customer findByEmail(String email);
	
	@Query("select c from Customer c where c.user.id=:userId")
	Customer findByUserId(@Param("userId")Long userId);
	
	@Query("from Customer")
	List<Customer> getAllCustomer();
	
	@Query("select count (*) from Customer")
	int getAllCustomerCount();
	
	@Query("select count (*) from Customer c where c.school.id=:school_id")
	int getAllCustomerCountForSchoolUser(@Param("school_id")long school_id);
	
	
	@Query("select count (*) from Customer WHERE date_added LIKE :date%")
	int getDailyCustomers(@Param("date")String date);
	
	@Query("select  c from Customer c where date_added > current_date - 7")
	List<Customer> getLastWeekCustomers();
	
}
