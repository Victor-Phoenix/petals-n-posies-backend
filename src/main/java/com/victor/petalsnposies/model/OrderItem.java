package com.victor.petalsnposies.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
	@Id
	@GeneratedValue
	private Long id;

	private Long flowerId;
	private String flowerName;
	private String variantType;
	private Double unitPrice;
	private Integer quantity;
	private Double lineTotal;

	@ManyToOne
	@JsonBackReference
	private Order order;

}
