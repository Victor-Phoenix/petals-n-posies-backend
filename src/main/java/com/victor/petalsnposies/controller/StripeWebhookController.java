package com.victor.petalsnposies.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.victor.petalsnposies.repository.OrderRepository;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        String endpointSecret = "whsec_6a11f428392a7240cfc78f95e71457abf23da8a501cab2f5831448ef7c3f7ebd"; // from Stripe dashboard

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

            if (session != null) {
                Order order = orderRepository.findByStripeSessionId(session.getId());
                if (order != null) {
                    order.setPaymentStatus("PAID");
                    orderRepository.save(order);
                }
            }
        }

        return ResponseEntity.ok("success");
    }
}