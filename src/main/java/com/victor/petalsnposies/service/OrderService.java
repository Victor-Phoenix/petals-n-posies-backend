
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
			
//			 double unitPrice = flower.getPrice() + variant.getPriceAdd();
//		        double lineTotal = unitPrice * item.getQuantity();

		        OrderItem orderItem = new OrderItem();
		        orderItem.setFlowerId(flower.getId());
		        orderItem.setFlowerName(flower.getName());
		        orderItem.setVariantType(variant.getType());
//		        orderItem.setUnitPrice(unitPrice);
		        orderItem.setQuantity(item.getQuantity());
//		        orderItem.setLineTotal(lineTotal);
		        orderItem.setOrder(order);

		        items.add(orderItem);
//		        total += lineTotal;
		    }

		    order.setOrderItems(items);
		    order.setTotalPrice(total);

		    return orderRepository.save(order);
		}
	
	
	public Order saveOrder(Order order) {
		if(order.getOrderItems() != null ) {
			for(OrderItem item : order.getOrderItems()) {
				item.setOrder(order);
			}
		}
		return orderRepository.save(order);
	}
	
	 public List<Order> getAllOrders() {
	        return orderRepository.findAll();
	    }

	    public Order getOrder(Long id) {
	        return orderRepository.findById(id).orElse(null);
	    }
}
