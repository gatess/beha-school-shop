package com.beha.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.Product;
import com.beha.dao.model.School;
import com.beha.dao.model.SpecialPrice;

public interface SpecialPriceRepository  extends JpaRepository<SpecialPrice, Long>, JpaSpecificationExecutor<SpecialPrice>{

	SpecialPrice findByProduct(Product product);
	SpecialPrice findByProductAndSchool(Product product,School school);
	SpecialPrice findByProductAndSchoolAndStatus(Product product,School school, Boolean status);
}
