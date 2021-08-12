package com.beha.dao.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Data
@Table(name = "product_image")
public class ProductImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "barcode")
	private Product product;
	
	@Column(name="file_path")
	private String filePath;
	
	@Column(name="height")
	private Integer height;
	
	@Column(name="width")
	private Integer width;
	
	@Lob
	private byte[] content;

	@Column(name="sort_order")
	private Integer sortOrder;

}
