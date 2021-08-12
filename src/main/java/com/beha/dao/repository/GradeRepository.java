package com.beha.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.Grade;
import com.beha.dao.model.School;

public interface GradeRepository extends JpaRepository<Grade, Long>, JpaSpecificationExecutor<Grade> {
	List<Grade> findBySchoolId(Long schoolId);
	Grade findByNameAndSchool(String name, School school);

}
