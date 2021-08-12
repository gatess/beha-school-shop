package com.beha.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.beha.dao.model.Customer;
import com.beha.dao.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{

	Product findByBarcode(String barcode);
	@Query("select p from Product p where p.name like %:name%")
	List<Product> searchByNameLike(@Param("name") String name);
	
	List<Product> findByNameContainingIgnoreCase(String name);
	List<Product> findByNameLike(String name);
	
	List<Product> findByNameContaining(String name);
	
	@Query("select count (*) from Product")
	int getAllProductCount();
	
	@Query("from Product ")
	List<Product> getAllProduct();
	
}
