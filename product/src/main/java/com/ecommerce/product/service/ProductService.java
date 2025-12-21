package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    public Product createProduct(ProductRequestDto dto);
    public Product getProduct(Long id);
    public Product updateProduct(Long id, ProductRequestDto dto);
    public List<Product> getAllProducts();
    public void deleteProduct(Long id);
}
