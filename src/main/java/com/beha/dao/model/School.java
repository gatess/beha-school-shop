package com.beha.dao.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "school")
public class School {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", length = 96)
	private String name;
	
	@Column(name = "subdomain", length = 96)
	private String subdomain;

	@Column(name = "city" , length = 18)
	private String city;

	@Column(name = "town" , length = 18)
	private String town;

	@Column(name = "campus" , length = 96)
	private String campus;
	
	@Column(name = "commission_rate")
	private Double commissionRate;
	
	@Column(name = "status")
	private Boolean status;

	@OneToMany(mappedBy = "school", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Grade> grades;
	
	@OneToMany(mappedBy = "school", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Teacher> teachers;
	
	@OneToMany(mappedBy = "school", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Customer> customers;
	
	@OneToOne(mappedBy = "school", fetch= FetchType.LAZY ,cascade = CascadeType.ALL )
    private SchoolUser schoolUser;
}
