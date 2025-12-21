package com.ecommerce.user.dto;

public record UpdateUserRequestDto(
        String username,
        String email,
        String phone
        ) { }
