package com.beha.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.IyzicoForm;

public interface IyzicoFormRepository extends JpaRepository<IyzicoForm, Long>, JpaSpecificationExecutor<IyzicoForm> {

}
