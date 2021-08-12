package com.beha.dao.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "iyzico_form")
public class IyzicoForm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "system_time")
	private Long systemTime;
	
	@Column(name = "conversation_id")
	private String conversationId;
	
	@Column(name = "error_code")
	private String errorCode;
	
	@Column(name = "error_message")
	private String errorMessage;
}
