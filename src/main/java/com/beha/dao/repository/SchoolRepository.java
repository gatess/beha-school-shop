package com.beha.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.School;

public interface SchoolRepository extends JpaRepository<School, Long>, JpaSpecificationExecutor<School>{
	
	List<School> findByTown(String town);
	List<School> findBySubdomainAndStatus(String subdomain, Boolean status);
	List<School> findByNameAndStatus(String name, Boolean status);
	List<School> findByStatus(Boolean status);
	List<School> findByTownAndCity(String town , String city);
	List<School> findByTownOrCity(String town , String city);
}
