package com.beha.controller.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Contact {

	  private String name;
	  private String lastName;
	  private int age;
	  private String objectID;
	  @JsonProperty("categories.​​​lvl0")
	  private String departmantCode;
	  @JsonProperty("categories.​​​lvl1")
	  private String departmantCode1;
	  
	  public Contact() {}

	public Contact(String name, String lastName, int age, String objectID,String departmantCode,String departmantCode1) {
		this.lastName=lastName;
		this.name=name;
		this.age=age;
		this.objectID=objectID;
		this.departmantCode=departmantCode;
		this.departmantCode1=departmantCode1;
	}

	}
