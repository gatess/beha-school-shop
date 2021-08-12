package com.beha.dao.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "order_history")
public class OrderHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "date_added")
	private Date createdDate;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "order_status_id")
	private OrderStatus orderStatus;

	//@NotNull
	@ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="notice")
	private boolean notice;
	
	@PrePersist
	public void prePersist() {
		setCreatedDate(new Date());
	}
}
