package com.ecommerce.user.dto;

import com.ecommerce.user.entity.Address;

import java.time.Instant;

public record CreateUserRequestDto(String username, String email, String phone, String role, Address address,
                                   Instant createdAt, Instant updatedAt) {
}

