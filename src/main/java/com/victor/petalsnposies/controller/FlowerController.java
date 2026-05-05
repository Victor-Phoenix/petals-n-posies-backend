package com.victor.petalsnposies.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victor.petalsnposies.dto.FlowerRequestDTO;
import com.victor.petalsnposies.dto.FlowerResponseDTO;
import com.victor.petalsnposies.model.Flower;
import com.victor.petalsnposies.service.FlowerService;

@RestController
@RequestMapping("/flower")
@CrossOrigin(origins = "*")
public class FlowerController {

	
	final private FlowerService flowerService;
	public FlowerController(FlowerService flowerService) {
		this.flowerService = flowerService;
	}
	
	@PostMapping("/addFlower")
	public Flower addFlower(@RequestBody FlowerRequestDTO flower) {
		return flowerService.saveFlower(flower);
	}
	
	@GetMapping("/getAll")
	public List<FlowerResponseDTO> getAllFlower(){
		return flowerService.getAllFlower();
	}
	
	@GetMapping("/{id}")
	public FlowerResponseDTO getFlowerById(@PathVariable Long id) {
		return flowerService.getById(id);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteById(@PathVariable Long id) {
		flowerService.deleteById(id);	
	}

	@DeleteMapping()
	public void deleteAll() {
		flowerService.deleteAll();
	}
	
//	@PutMapping("/update")
//	public void updateFlower(@RequestBody FlowerRequestDTO flower) {
//		flowerService.updateFlower(flower);
//	}
}	
