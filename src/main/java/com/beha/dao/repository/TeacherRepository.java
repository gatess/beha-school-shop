package com.beha.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.School;
import com.beha.dao.model.Teacher;

public interface TeacherRepository  extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher>{
	//AdminUser findbyEmailOrUserName(String email);
	Teacher findByEmail(String email);
	
	List<Teacher> findBySchool(School school);
	
}
