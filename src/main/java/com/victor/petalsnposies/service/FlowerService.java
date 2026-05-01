package com.victor.petalsnposies.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.victor.petalsnposies.model.Flower;
import com.victor.petalsnposies.model.Variant;
import com.victor.petalsnposies.repository.FlowerRepository;

@Service
public class FlowerService {
	
	private final FlowerRepository flowerRepo;
	
	public FlowerService (FlowerRepository flowerRepo) {
		this.flowerRepo = flowerRepo;
	}
	
	
	private int orderOf(String type) {
		return switch(type.toLowerCase()) {
			case "standard"->1;
			case "deluxe" ->2;
			case "premium" ->3;
			default -> 99;
		};
	}
	public Flower saveFlower(@RequestBody Flower flower) {
		
//		return flowerRepo.save(flower);
		  // Set the parent on each variant
	    if (flower.getVariants() != null) {
	        for (Variant v : flower.getVariants()) {
	            v.setFlower(flower);
	        }
	    }

	    return flowerRepo.save(flower);
	}
	
	public Flower updateFlower(Flower flower) {
		Flower existingFlower = flowerRepo.findById(flower.getId()).orElseThrow(()-> new RuntimeException("Resoure not found"));
		
		
			
			existingFlower.setName(flower.getName());
			existingFlower.setImageUrl(flower.getImageUrl());
			
			existingFlower.setSKU(flower.getSKU());
			existingFlower.setCategories(flower.getCategories());
		
			flower.getVariants().sort((a,b)-> orderOf(a.getType()) - orderOf(b.getType()) );
			
			 // --- FIXED VARIANT UPDATE LOGIC ---
		    List<Variant> existingVariants = existingFlower.getVariants();
		    existingVariants.clear(); // remove old ones safely
			
		    for (Variant v : flower.getVariants()) {
		        v.setFlower(existingFlower); // maintain relationship
		        existingVariants.add(v);  // add to existing list
		    }
		return flowerRepo.save(existingFlower);
	}
	public List<Flower> getAllFlower(){
		List<Flower> flower = flowerRepo.findAll();
		for(Flower element: flower) {
			element.getVariants().sort((a,b)-> orderOf(a.getType()) - orderOf(b.getType()) );
		}
		return flower;
	}
	public Flower getById(Long id)
	{
		Flower flower = flowerRepo.findById(id).orElseThrow(()-> new RuntimeException("Flower Not Found"));
		flower.getVariants().sort((a,b)-> orderOf(a.getType()) - orderOf(b.getType()) );
		return  flower;
	}
	
	public void deleteFlower(Flower flower) {
		flowerRepo.delete(flower);
	}
	
	public void deleteById(Long id) {
		flowerRepo.deleteById(id);
	}
	public void deleteAll() {
		flowerRepo.deleteAll();
	}
}
