package com.beha.dao.model;

public enum Role {
	ROLE_CUSTOMER(0,"Customer"),
	ROLE_ADMIN(1,"Admin");
	private int value;
	private String description;
	
	private Role(int value, String description) {
		this.value = value;
		this.description = description;
		
	}
	
	public int getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}
}
