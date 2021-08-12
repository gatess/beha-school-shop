package com.beha.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.IyzicoPayment;

public interface IyzicoPaymentRepository extends JpaRepository<IyzicoPayment, Long>, JpaSpecificationExecutor<IyzicoPayment> {

}
