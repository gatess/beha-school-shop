package com.beha.dao.model;

public enum OrderStatus {
	STATUS_DELIVERED(0 ,"Sipariş Teslim Edildi" ),
	STATUS_PAYMENT_COMPLETE(1, "Ödeme Tamamlandı"),
	STATUS_REFUND(2,"Geri Ödendi" ),
	STATUS_CANCEL(3,"İptal edildi"),
	STATUS_PREPARE(4,"Paket Hazırlanıyor"),
	STATUS_INVALID(5,"Geçersiz"),
	STATUS_AWATING_PAYMENT(6,"Ödeme Bekleniyor"),
	STATUS_DELIVERED_TO_CARGO(7,"Kargoya Teslim Edildi"),
	PACKAGE_READY(8,"Paket Hazır/Mağazada Bekliyor"),
	STATUS_PAYMENT_ERROR(9,"Ödeme Alınamadı.");
	private Integer value;
	private String description;
	
	private OrderStatus(Integer value, String description) {
		this.value = value;
		this.description = description;
		
	}
	
	public Integer getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}
	
	public static OrderStatus getByValue(Integer value) {
		for(OrderStatus orderStatus : OrderStatus.values()) {
			if(orderStatus.getValue().equals(value)) {
				return orderStatus;
			}
		}
		return null;
	}


}
