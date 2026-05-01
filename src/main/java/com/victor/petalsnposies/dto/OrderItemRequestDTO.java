package com.victor.petalsnposies.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemRequestDTO {
    private Long flowerId;
    private String variantType;
    private Integer quantity;
}