package com.ecommerce.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/fallback/products")
    public ResponseEntity<FallbackResponseDto> fallbackProducts() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new FallbackResponseDto(HttpStatus.SERVICE_UNAVAILABLE.value(), "Could not fetch products. Please try again later"));
    }

    @GetMapping("/fallback/users")
    public ResponseEntity<FallbackResponseDto> fallbackUsers() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new FallbackResponseDto(HttpStatus.SERVICE_UNAVAILABLE.value(), "Could not fetch users. Please try again later"));
    }

    @GetMapping("/fallback/orders")
    public ResponseEntity<FallbackResponseDto> fallbackOrders() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new FallbackResponseDto(HttpStatus.SERVICE_UNAVAILABLE.value(), "Could not fetch orders. Please try again later"));
    }
}
