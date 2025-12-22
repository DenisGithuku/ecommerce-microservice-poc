package com.ecommerce.order.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record ProductResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        Instant createdAt,
        Instant updatedAt
) {}
