package com.victor.petalsnposies.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victor.petalsnposies.dto.OrderRequestDTO;
import com.victor.petalsnposies.model.Order;
import com.victor.petalsnposies.model.OrderStatus;
import com.victor.petalsnposies.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	public OrderService orderService;
	
	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO dto){
		Order order = orderService.createOrder(dto);
		return ResponseEntity.ok(order);
	}
	
	@PostMapping("/{id}/pay")
    public ResponseEntity<String> pay(@PathVariable Long id) {
        String stripeUrl = orderService.initiatePayment(id);
        return ResponseEntity.ok(stripeUrl);
    }
	
	@GetMapping("/{sessionId}")
	public ResponseEntity<Order> findOrderBySessionId(@PathVariable String sessionId){
		Order order = orderService.getOrderBySessionId(sessionId);
		return ResponseEntity.ok(order);
	}
	@GetMapping("/getAll")
	public ResponseEntity<List<Order>> getAllOrder(){
		List<Order> orders = orderService.getAllOrders();
		if(orders.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
	
		return ResponseEntity.ok(orders);
	}
	
	@PatchMapping("/setOrderStatus-{id}")
	public ResponseEntity<Order> setOrderStatus(@PathVariable Long id, @RequestBody Map<String,String> body){
		Order order = orderService.getOrder(id);
		String status = body.get("status");
		order.setOrderStatus(OrderStatus.valueOf(status));
//		Create a save function in service to save object. 
//		orderService.save(order);
		return null;
	}
}
