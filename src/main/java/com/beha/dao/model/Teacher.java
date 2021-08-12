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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
@Data
@Entity
@Table(name = "teacher")
public class Teacher {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "name", length = 80)
	private String name;
	
	@NotBlank
	@Column(name = "email" , length = 96)
	private String email;
	
	@NotBlank
	@Column(name = "password" , length = 100)
	private String password;
	
	@Column(name = "date_added")
	private Date createdDate;

	@Column(name = "date_modified")
	private Date updatedDate;
	
    @NotNull
	@ManyToOne
	@JoinColumn(name = "school_id")
	private School school;
	
	@OneToMany(mappedBy = "teacher")
    List<Lesson> lessons;
	
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
