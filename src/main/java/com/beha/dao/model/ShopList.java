package com.beha.dao.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "shop_list")
public class ShopList {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
    @JoinColumn(name = "lesson_id")  
	private Lesson lesson;
	

	@ManyToOne
    @JoinColumn(name = "barcode")  
	private Product product;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "price")
	private double price;
	
	
	
}
