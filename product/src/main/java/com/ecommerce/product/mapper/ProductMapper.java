package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.entity.Product;

public class ProductMapper {

    public static Product mapToEntity(ProductRequestDto dto) {
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .quantity(dto.quantity())
                .build();
    }

    public static ProductResponseDto mapToDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static void updateEntity(Product product, ProductRequestDto dto) {
        if (dto.name() != null) product.setName(dto.name());
        if (dto.description() != null) product.setDescription(dto.description());
        if (dto.price() != null) product.setPrice(dto.price());
        if (dto.quantity() != null) product.setQuantity(dto.quantity());
    }
}
