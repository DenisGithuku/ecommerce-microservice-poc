package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.exception.DuplicateProductException;
import com.ecommerce.product.exception.ResourceNotFoundException;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.ProductRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    public Product createProduct(ProductRequestDto dto) {
        if (repository.existsByName(dto.name())) { throw new DuplicateProductException(dto.name()); }
        Product product = ProductMapper.mapToEntity(dto);
        return repository.save(product);
    }

    public Product updateProduct(Long id, ProductRequestDto dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, id.toString()));

        ProductMapper.updateEntity(product, dto);
        return repository.save(product);
    }

    public Product getProduct(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, id.toString()));
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public void deleteProduct(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(Product.class, id.toString());
        }
        repository.deleteById(id);
    }
}
