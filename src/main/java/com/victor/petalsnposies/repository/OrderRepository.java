package com.victor.petalsnposies.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PathVariable;

import com.victor.petalsnposies.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	Optional<Order> findByStripeSessionId(String stripeSessionId);
}
