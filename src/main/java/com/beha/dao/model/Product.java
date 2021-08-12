package com.beha.dao.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {
	@Id
	@Column(name = "barcode", length = 64)
	private String barcode;
	
	@NotBlank
	@Column(name = "name", length = 255)
	private String name;

	
	@Column(name = "quantity")
	private Integer quantity;

	@ManyToOne
	@JoinColumn(name = "manufacturer_id")
	private Manufacturer manufacturer;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "tax_id")
	@Enumerated(EnumType.ORDINAL)
	private Tax tax;

	@Column(name = "status")
	private Boolean status;

	@Column(name = "date_added")
	private Date createdDate;

	@Column(name = "date_modified")
	private Date updatedDate;
	
	@OneToMany(mappedBy = "product",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	private List<ProductImage> productImages;
	
	@OneToMany(mappedBy = "product")
	private List<ShopList> shopLists;
	
//	@OneToMany(mappedBy = "product",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
//	private List<OrderDetail> orderDetail;

	@PrePersist
	public void prePersist() {
		setCreatedDate(new Date());
		setUpdatedDate(new Date() );
	}
	
	@PreUpdate
	public void preUpdate() {
		setUpdatedDate(new Date() );
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (barcode == null) {
			if (other.barcode != null)
				return false;
		} else if (!barcode.equals(other.barcode))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
		return result;
	}

}
