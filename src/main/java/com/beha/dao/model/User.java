package com.beha.dao.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	@NotBlank
	@Size(max = 50)
	private String name;

	@Column(name = "surname")
	@NotBlank
	@Size(max = 50)
	private String surName;

	@Column(name = "email")
	@NotBlank
	@Size(max = 70)
	@Email
	private String email;

	@Column(name = "password")
	@NotBlank
	@Size(max = 70)
	private String password;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "role_id")
	private Role role;
	
	@OneToOne(mappedBy = "user", fetch= FetchType.LAZY ,cascade = CascadeType.ALL )
    private Customer customer;

	
}
