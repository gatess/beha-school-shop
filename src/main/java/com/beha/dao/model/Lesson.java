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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lesson")
@Getter
@Setter
public class Lesson {

	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long id;
	   
	    @NotNull
	    @Column(name = "name", length = 64)
		private String name;
	   
	    @ManyToOne
		@JoinColumn(name = "grade_id")
	    private Grade grade;
	    
	    @OneToMany(mappedBy = "lesson" ,fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	    @Setter(AccessLevel.NONE)
		private List<ShopList> shopLists;
	    
		
	    @JoinTable(name = "teacher_lesson", joinColumns = { @JoinColumn(name = "lesson_id", referencedColumnName = "id") }, 
	               inverseJoinColumns = { @JoinColumn(name = "teacher_id",referencedColumnName = "id") })
	    @ManyToOne
		private Teacher teacher;
		
		
		public void setShopLists(List<ShopList> shopLists) {
			this.shopLists = shopLists;
			this.shopLists.forEach(s->{
				s.setLesson(this);
			});
		}
}
