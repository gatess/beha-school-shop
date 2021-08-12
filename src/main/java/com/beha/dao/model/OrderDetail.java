package com.beha.dao.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "order_detail")
public class OrderDetail {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	//@NotNull
	@ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
	
	@NotNull
    @ManyToOne
    @JoinColumn(name = "barcode", nullable = false)
    private Product product;
	
	@NotNull
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "price")
	private Double price;

}
