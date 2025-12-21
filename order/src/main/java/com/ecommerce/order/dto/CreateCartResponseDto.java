package com.ecommerce.order.dto;

import java.util.List;

public record CreateCartResponseDto(Long id, String userId, List<Long> productIds) {}
