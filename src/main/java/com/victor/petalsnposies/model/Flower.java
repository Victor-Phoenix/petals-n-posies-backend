package com.victor.petalsnposies.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="flowers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Flower {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	String name;
	
	String imageUrl;
	
	
	@OneToMany(mappedBy = "flower", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	@OrderColumn(name = "variant_order")
	List<Variant> variants;
	
	String SKU;

	
	@ElementCollection
	List<String> categories;
	
	public Variant findVariant(String variantType) {
		 if (variants == null) {
		        return null;
		    }
		for(int i =0; i < variants.size(); i++) {
			Variant v =  variants.get(i);
			if (v.getType().equalsIgnoreCase(variantType)) {
	            return v;
	        }
		}
		 return null;
	}
}
