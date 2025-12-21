package com.ecommerce.product.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequestDto(
        String name,
        String description,
        BigDecimal price,
        Integer quantity
) {}
