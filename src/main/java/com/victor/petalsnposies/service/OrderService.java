
package com.victor.petalsnposies.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.victor.petalsnposies.dto.OrderItemRequestDTO;
import com.victor.petalsnposies.dto.OrderRequestDTO;
import com.victor.petalsnposies.model.Flower;
import com.victor.petalsnposies.model.Order;
import com.victor.petalsnposies.model.OrderItem;
import com.victor.petalsnposies.model.Variant;
import com.victor.petalsnposies.repository.FlowerRepository;
import com.victor.petalsnposies.repository.OrderRepository;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import com.stripe.model.checkout.Session;
import com.stripe.model.checkout.Session.AutomaticTax;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.BillingAddressCollection;
import com.stripe.param.checkout.SessionCreateParams.ShippingAddressCollection;
import com.stripe.param.checkout.SessionCreateParams.ShippingAddressCollection.AllowedCountry;
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final FlowerRepository flowerRepository;
	public OrderService(OrderRepository orderRepository, FlowerRepository flowerRepository) {
		this.orderRepository = orderRepository;
		this.flowerRepository = flowerRepository;
	}
	
	public Order createOrder(OrderRequestDTO dto) {
		Order order = new Order();
		List<OrderItem> items = new ArrayList<>();
		
		double total = 0;
		
		for(OrderItemRequestDTO item: dto.getItems()) {
			
			Flower flower = flowerRepository.findById(item.getFlowerId()).orElseThrow();
			Variant variant = flower.findVariant(item.getVariantType());
			
			 double unitPrice =  variant.getPrice();
		        double lineTotal = unitPrice * item.getQuantity();

		        OrderItem orderItem = new OrderItem();
		        orderItem.setFlowerId(flower.getId());
		        orderItem.setFlowerName(flower.getName());
		        orderItem.setVariantType(variant.getType());
		        orderItem.setUnitPrice(unitPrice);
		        orderItem.setQuantity(item.getQuantity());
		        orderItem.setLineTotal(lineTotal);
		        orderItem.setOrder(order);
		        

		        items.add(orderItem);
		        total += lineTotal;
		    }

		    order.setOrderItems(items);
		    order.setTotalPrice(total);
		    order.setCustomerName(dto.getCustomerName());
		    order.setDeliveryDate(dto.getDeliveryDate());
		    order.setPaymentStatus("PENDING");

		    return orderRepository.save(order);
		}
	
	
	private String createStripeCheckoutSession(Order order) {
		try {
			SessionCreateParams.Builder builder = SessionCreateParams.builder()
					.setMode(SessionCreateParams.Mode.PAYMENT)
					.setSuccessUrl("http://localhost:5173/success?session_id={CHECKOUT_SESSION_ID}")
	                .setCancelUrl("http://localhost:5173/cancel")
	                .setBillingAddressCollection(BillingAddressCollection.REQUIRED)
					.setShippingAddressCollection(SessionCreateParams.ShippingAddressCollection.builder().addAllowedCountry(AllowedCountry.US).build());
			builder.setAutomaticTax(SessionCreateParams.AutomaticTax.builder()
	                .setEnabled(true)
	                .build());
					builder.setPhoneNumberCollection(SessionCreateParams.PhoneNumberCollection.builder().setEnabled(true).build());
					
					

	        for (OrderItem item : order.getOrderItems()) {
	            builder.addLineItem(
	                SessionCreateParams.LineItem.builder()
	                    .setQuantity(item.getQuantity().longValue())
	                    .setPriceData(
	                        SessionCreateParams.LineItem.PriceData.builder()
	                            .setCurrency("usd")
	                            .setUnitAmount((long)(item.getUnitPrice() * 100))
	                            // convert to cents
	                            .setProductData(
	                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
	                                    .setName(item.getFlowerName() + " - " + item.getVariantType())
	                                    .build()
	                            )
	                            .build()
	                    )
	                    .build()
	            );
	        }
	        Session session = Session.create(builder.build());
	        order.setStripeSessionId(session.getId());
	        System.out.println("Created session ID: " + session.getId());

	        orderRepository.save(order);
			
	        return session.getUrl();
			
		}catch(Exception e) {
			throw new RuntimeException("Stripe session creation failed");
		}
	}
	
	
	public String initiatePayment(Long orderId) {
	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new RuntimeException("Order not found"));
	    

	    if ("PAID".equals(order.getPaymentStatus())) {
	        throw new RuntimeException("Order already paid");
	    }

	    return createStripeCheckoutSession(order);
	}
	
	
	
	
	 public List<Order> getAllOrders() {
	        return orderRepository.findAll();
	    }

	    public Order getOrder(Long id) {
	        return orderRepository.findById(id).orElse(null);
	    }
}
