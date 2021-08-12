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
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "date_added")
	private Date createdDate;

	@Column(name = "date_modified")
	private Date updatedDate;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "order_status_id")
	private OrderStatus orderStatus;

	@Column(name = "total")
	private Double total;
	
	@Column(name = "commission_amount")
	private Double commissionAmount;

	@NotBlank
	@Column(name = "student_name", length = 48)
	private String studentName;

	@ManyToOne
	//@NotNull
	@JoinColumn(name = "customer_id")
	private Customer customer;

	//@NotNull
	@ManyToOne
    @JoinColumn(name = "grade_id", nullable = false)
	private Grade grade;
	
	@ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
	private Address address;
	
	@OneToMany(mappedBy = "order",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	private List<OrderDetail> orderDetail;
	
	@OneToMany(mappedBy = "order",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	private List<OrderHistory> orderHistory;
	
	@PrePersist
	public void prePersist() {
		setCreatedDate(new Date());
		setUpdatedDate(new Date() );
	}
	
	@PreUpdate
	public void preUpdate() {
		setUpdatedDate(new Date() );
	}

}
