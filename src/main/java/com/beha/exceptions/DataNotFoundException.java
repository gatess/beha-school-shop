package com.beha.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DataNotFoundException extends Exception {

	public DataNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
