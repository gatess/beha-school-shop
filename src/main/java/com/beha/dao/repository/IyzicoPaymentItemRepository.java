package com.beha.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.beha.dao.model.IyzicoPaymentItem;

public interface IyzicoPaymentItemRepository extends JpaRepository<IyzicoPaymentItem, Long>, JpaSpecificationExecutor<IyzicoPaymentItem> {

}
