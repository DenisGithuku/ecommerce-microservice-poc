package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.dto.ValidateProductResponseDto;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RefreshScope
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    @Value("${status.name}")
    private String status;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto dto) {
        Product product = service.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.mapToDto(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        Product product = service.getProduct(id);
        return ResponseEntity.ok(ProductMapper.mapToDto(product));
    }

    @GetMapping("/validate/{id}")
    public ResponseEntity<ValidateProductResponseDto> existsProduct(@PathVariable Long id) {
        Long userId = service.getProduct(id).getId();
        var responseDto = ValidateProductResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("Product with id: " + id + " exists")
                .data(userId)
                .build();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = service.getAllProducts()
                .stream()
                .map(ProductMapper::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDto dto) {

        Product updated = service.updateProduct(id, dto);
        return ResponseEntity.ok(ProductMapper.mapToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok(status);
    }
}
