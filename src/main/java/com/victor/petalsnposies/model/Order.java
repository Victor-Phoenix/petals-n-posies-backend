package com.victor.petalsnposies.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="orders")
public class Order {
	@Id
	@GeneratedValue()
	private Long id;
	
	private String customerName;

	private String deliveryDate;
	private Double totalPrice;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus 	OrderStatus;
	
	@Column(name = "stripe_session_id")
	private String stripeSessionId;
	private String paymentStatus; 
	private String shippingAddress;
	private String shippingCity;
	private String shippingState;
	private String shippingPostalCode;
	private String phoneNumber;
	
	
	
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<OrderItem> orderItems;
	
	
}
