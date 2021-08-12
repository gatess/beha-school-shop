package com.beha.dao.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "school_user")
public class SchoolUser {
	
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
	private String surname;

	@Column(name = "email")
	@NotBlank
	@Size(max = 70)
	@Email
	private String email;

	@Column(name = "password")
	@NotBlank
	@Size(max = 70)
	private String password;
	
	@OneToOne
    @JoinColumn(name = "school_id")
	//@MapsId
    private School school;

}
