package com.beha.dao.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

//@SecondaryTable(name = "address",pkJoinColumns = @PrimaryKeyJoinColumn(name= "customer_id"))
@Entity
@Data
@Table(name = "customer")
public class Customer {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "name", length = 50)
	private String name;

	@Column(name = "surname", length = 50)
	private String surname;
	
	@NotBlank
	@Column(name = "email" , length = 96)
	private String email;
	
	
	@Column(name = "date_added")
	private Date createdDate;

	@Column(name = "date_modified")
	private Date updatedDate;
	
	@OneToMany(mappedBy = "customer" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	private List<Address> address;
	
	@OneToMany(mappedBy = "customer",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	private List<Order> orders;
	
	@OneToOne
    @JoinColumn(name = "user_id")
	//@MapsId
    private User user;
	
	@ManyToOne
	@JoinColumn(name = "school_id")
	private School school;
 
	
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
