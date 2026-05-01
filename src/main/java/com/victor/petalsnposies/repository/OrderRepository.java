package com.victor.petalsnposies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victor.petalsnposies.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
