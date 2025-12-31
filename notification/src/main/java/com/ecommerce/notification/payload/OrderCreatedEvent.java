package com.ecommerce.notification.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String userId;
    private List<CartItemDto> items;
    private BigDecimal totalAmount;
    private Instant createdAt;
}
