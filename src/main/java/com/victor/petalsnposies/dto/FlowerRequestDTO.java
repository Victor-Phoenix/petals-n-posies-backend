package com.victor.petalsnposies.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FlowerRequestDTO {
    private String name;
    private String imageUrl;
    private String sku;
    private List<String> categories;
    private List<VariantRequestDTO> variants;
}
