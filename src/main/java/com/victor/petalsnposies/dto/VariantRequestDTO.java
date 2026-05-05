package com.victor.petalsnposies.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VariantRequestDTO {
	private String type;
	private double price;
	private String description;
}
