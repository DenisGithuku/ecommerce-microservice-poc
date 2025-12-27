package com.ecommerce.gateway.controller;


public record FallbackResponseDto(
        int status,
        String message
) {}
