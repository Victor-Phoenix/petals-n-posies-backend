package com.victor.petalsnposies.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRequestDTO {
	  private String deliveryDate;	
	  private String customerName;
	    private List<OrderItemRequestDTO> items;
}
