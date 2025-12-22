package com.ecommerce.gateway.controller;


public record FallbackProductsResponseDto(
        int status,
        String message
) {}
