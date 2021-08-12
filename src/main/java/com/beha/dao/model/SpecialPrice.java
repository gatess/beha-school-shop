package com.beha.dao.model;

import java.util.Date;
import java.util.List;

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
@Table(name = "special_price")
public class SpecialPrice {
	
	 @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long id;
	   
	   @ManyToOne
	   @JoinColumn(name="barcode")
	   private Product product;
	   
	   @Column(name = "status")
	   private Boolean status;
	   
	   @Column(name = "price")
	   private Double price;
	   
	   @ManyToOne
	   @JoinColumn(name = "school_id")
	   private School school;
	   
	   

}
