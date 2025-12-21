package com.ecommerce.order.dto;

import java.util.List;

public record CartResponseDto(
        Long id,
        String userId,
        List<CartItemDto> items,
        boolean checkedOut
) {}

