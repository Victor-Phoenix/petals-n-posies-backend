package com.victor.petalsnposies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.victor.petalsnposies.model.Order;
import com.victor.petalsnposies.model.OrderStatus;
import com.victor.petalsnposies.repository.OrderRepository;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    @Autowired
    private OrderRepository orderRepository;
    
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
    	System.out.println("WEBHOOK HIT");

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject()
                    .orElse(null);
            Session.CollectedInformation collected = session.getCollectedInformation();
            System.out.println("Webhook session ID: " + session.getId());	
            Session.CustomerDetails customerDetails = session.getCustomerDetails();
            
            String customerName = customerDetails.getName();
            String shippingAddress = collected.getShippingDetails().getAddress().getLine1();
            String shippingCity= collected.getShippingDetails().getAddress().getCity();
            String shippingState= collected.getShippingDetails().getAddress().getState();
            String shippingPostalCode = collected.getShippingDetails().getAddress().getPostalCode();
            String phoneNumer =customerDetails.getPhone();
            System.out.printf("Name %s%n Address %s%n City %s%n State %s%n Postal %s%n " , customerName, shippingAddress, shippingCity, shippingState, shippingPostalCode);
            
            if (session != null) {
                Order order = orderRepository.findByStripeSessionId(session.getId()).orElseThrow(()-> new RuntimeException("Resource not Found"));
                // TODO: Make OrderService populate order with correct information
                order.setCustomerName(customerName);
                order.setShippingAddress(shippingAddress);
                order.setShippingCity(shippingCity);
                order.setShippingState(shippingState);
                order.setShippingPostalCode(shippingPostalCode);
                order.setPhoneNumber(phoneNumer);
                order.setOrderStatus(OrderStatus.PENDING);
                
                if (order != null) {
                    order.setPaymentStatus("PAID");
                    orderRepository.save(order);
                }
            }
        }

        return ResponseEntity.ok("success");
    }
}