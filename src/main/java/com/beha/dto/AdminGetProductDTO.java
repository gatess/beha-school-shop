package com.beha.dto;

import lombok.Data;

@Data
public class AdminGetProductDTO extends GetProductDTO {
		private long tax;
		private boolean status;
}
