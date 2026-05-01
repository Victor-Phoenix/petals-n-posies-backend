package com.victor.petalsnposies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victor.petalsnposies.model.Flower;

public interface FlowerRepository extends JpaRepository<Flower, Long>{

	
}
