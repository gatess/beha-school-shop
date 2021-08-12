package com.beha.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.Role;
import com.beha.dao.model.User;

public interface UserRepository  extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{

	boolean existsByEmail(String email);
	User findByEmail(String email);
	boolean existsByEmailAndPassword(String email,String password);
	boolean existsByEmailAndPasswordAndRole(String email,String password,Role role);
}
