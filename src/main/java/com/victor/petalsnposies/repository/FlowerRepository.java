package com.victor.petalsnposies.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.victor.petalsnposies.model.Flower;

public interface FlowerRepository extends JpaRepository<Flower, Long>{

	@EntityGraph(attributePaths = {"variants", "categories"})
    @Override
    List<Flower> findAll();
}
