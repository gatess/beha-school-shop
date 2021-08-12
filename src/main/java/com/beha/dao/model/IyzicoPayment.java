package com.beha.dao.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "iyzico_payment")
public class IyzicoPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "payment_id")
	private String paymentId;
	
	@Column(name = "fraud_status")
	private int fraudStatus;
	
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
	
	@Column(name = "iyzico_comission_rate_amount")
	private BigDecimal iyziCommissionRateAmount;
	
	@OneToMany(mappedBy = "iyzicoPayment",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	private List<IyzicoPaymentItem> iyzicoPaymentItem;
}
