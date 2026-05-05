package com.victor.petalsnposies.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.victor.petalsnposies.dto.FlowerRequestDTO;
import com.victor.petalsnposies.dto.FlowerResponseDTO;
import com.victor.petalsnposies.dto.VariantRequestDTO;
import com.victor.petalsnposies.dto.VariantResponseDTO;
import com.victor.petalsnposies.model.Flower;
import com.victor.petalsnposies.model.Variant;
import com.victor.petalsnposies.repository.FlowerRepository;
import com.victor.petalsnposies.repository.OrderRepository;

@Service
public class FlowerService {

    private final OrderRepository orderRepository;
	
	private final FlowerRepository flowerRepo;
	
	private static final Set<String> VALID_CATEGORIES = Set.of(
		    "Romance", "Birthday", "Congratulations", "Get Well",
		    "Thank You", "New Baby", "Funeral"
		);
	
	
	public FlowerService (FlowerRepository flowerRepo, OrderRepository orderRepository) {
		this.flowerRepo = flowerRepo;
		this.orderRepository = orderRepository;
	}
	
	
	private int orderOf(String type) {
		return switch(type.toLowerCase()) {
			case "standard"->1;
			case "deluxe" ->2;
			case "premium" ->3;
			default -> 99;
		};
	}
	
	private void validCategory(List<String> categories) {
		
		for(String c: categories ) {
			if(!VALID_CATEGORIES.contains(c)) {
				   throw new RuntimeException("Invalid category: " + c);			}
		}
	}
	
	private FlowerResponseDTO toResponseDTO(Flower flower) {
		FlowerResponseDTO dto = new FlowerResponseDTO();
		dto.setId(flower.getId());
		dto.setName(flower.getName());
		dto.setImageUrl(flower.getImageUrl());
		dto.setCategories(flower.getCategories());
		
		List<Variant> var = flower.getVariants();
		List<VariantResponseDTO> variantDTOList = new ArrayList<VariantResponseDTO>();
		for(Variant element : var) {
			VariantResponseDTO variantDTO = new VariantResponseDTO();
			variantDTO.setType(element.getType());
			variantDTO.setPrice(element.getPrice());
			variantDTO.setDescription(element.getDescription());
			variantDTOList.add(variantDTO);
		}
		dto.setVariants(variantDTOList);
		return dto;
	}
	
	private Flower toEntity(FlowerRequestDTO dto) {
		Flower flower = new Flower();
		List<Variant> variants = new ArrayList<>();
		flower.setName(dto.getName());
		flower.setImageUrl(dto.getImageUrl());
		
		flower.setSKU(dto.getSku());
		
		validCategory(dto.getCategories());
		flower.setCategories(dto.getCategories());
		
		for(VariantRequestDTO element : dto.getVariants()) {
			Variant varaint = new Variant();
			varaint.setType(element.getType());
			varaint.setPrice(element.getPrice());
			varaint.setDescription(element.getDescription());
			varaint.setFlower(flower);
			variants.add(varaint);
		}
		flower.setVariants(variants);
		return flower;
	}
	
	
	public Flower saveFlower(@RequestBody FlowerRequestDTO flower) {
		

	    return flowerRepo.save(toEntity(flower));
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
	
	public List<FlowerResponseDTO> getAllFlower(){
		List<FlowerResponseDTO> flower = flowerRepo.findAll().stream().map(this::toResponseDTO).toList();
		for(FlowerResponseDTO element: flower) {
			element.getVariants().sort((a,b)-> orderOf(a.getType()) - orderOf(b.getType()) );
		}
		return flower;
	}
	
	public FlowerResponseDTO getById(Long id)
	{
		Flower flower = flowerRepo.findById(id).orElseThrow(()-> new RuntimeException("Flower Not Found"));
		flower.getVariants().sort((a,b)-> orderOf(a.getType()) - orderOf(b.getType()) );
		return toResponseDTO(flower);
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
