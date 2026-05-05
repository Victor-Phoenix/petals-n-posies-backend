package com.victor.petalsnposies.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FlowerResponseDTO {

	private Long id;
    private String name;
    private String imageUrl;
    private List<String> categories;
    private List<VariantResponseDTO> variants;
}
