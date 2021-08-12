package com.beha.dao.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
@Table(name = "grade")
public class Grade {
	
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long id;
	   
		@Column(name = "name", length = 64)
		private String name;
		
		@ManyToOne
		@JoinColumn(name = "school_id")
		private School school;

		@OneToMany(mappedBy = "grade",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
		private List<Lesson> lessons;
}
