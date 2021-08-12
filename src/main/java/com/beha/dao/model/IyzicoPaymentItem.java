package com.beha.dao.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "iyzico_payment_item")
public class IyzicoPaymentItem {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	//@NotNull
	@ManyToOne
    @JoinColumn(name = "iyzico_payment_id", nullable = false)
    private IyzicoPayment iyzicoPayment;
	
	@Column(name = "payment_transaction_id")
	private String paymentTransactionId;
	
	@Column(name = "paid_price")
	private BigDecimal paidPrice;
	
	@Column(name = "paid_price_converted_payout")
	private BigDecimal paidPriceConvertedPayout;
	
	

}
