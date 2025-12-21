package com.ecommerce.product.exception;

public class DuplicateProductException extends RuntimeException {
    public DuplicateProductException(String name) {
        super("Product: " + name + " exists already!");
    }
}
