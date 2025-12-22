package com.ecommerce.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/fallback/products")
    public ResponseEntity<FallbackProductsResponseDto> fallbackProducts() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new FallbackProductsResponseDto(HttpStatus.SERVICE_UNAVAILABLE.value(), "Could not fetch products!"));
    }
}
